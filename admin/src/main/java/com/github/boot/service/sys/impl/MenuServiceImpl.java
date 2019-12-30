package com.github.boot.service.sys.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.request.sys.SysMenuParams;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.model.sys.SysMenu;
import com.github.boot.service.sys.MenuService;
import com.github.boot.service.sys.SysMenuService;
import com.github.boot.sys.EnumSysMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public  List<MenuNodeResponse> selectMenuList() {
        // 从根据节点获取， 所有的类型。
       return sysMenuService.buildMenuNode(
               null,
               EnumSysMenu.MenuType.ALL.getKey(),
               EnumSysMenu.MenuType.ALL.getKey()
       );
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMenu(Long id) {
        List<Long> removeIds = new ArrayList<>();
        removeIds.add(id);
        bathDeleteChild(id, removeIds);

        sysMenuService.removeByIds(removeIds); // 删除所有子节点
    }

    private void bathDeleteChild(Long id, List<Long> removeIds) {

        List<SysMenu> sysMenus = sysMenuService.list(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)
        );
        if (CollectionUtil.isEmpty(sysMenus)) return;

        for (SysMenu sysMenu : sysMenus) {
            removeIds.add(sysMenu.getId());
            bathDeleteChild(sysMenu.getId(), removeIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(SysMenuParams menuParams) {
        SysMenu menu = new SysMenu();
        BeanUtil.copyProperties(menuParams,menu);

        validatorMenuParams(menu); //校验参数

        if (menu.getParentId() > 0) checkMenuNode(menu); // 校验节点类型

        menu.setCreateUser(0L);
        menu.setCreateTime(new Date());
        sysMenuService.save(menu);
    }

    private void checkMenuNode(SysMenu menu) {

        SysMenu dbMenu = sysMenuService.getById(menu.getParentId());
        if (ObjectUtil.isNull(dbMenu)) throw new PlantException("上级菜单不存在");

        if(menu.getMenuType().equals(EnumSysMenu.MenuType.BUTTON.getKey())){
            String check = existsPermissionCode(menu.getParentId(),null, menu.getPermissionCode());
            if(StringUtils.isNotEmpty(check))
                throw new PlantException(String.format("权限码 %s 已存在~",check));
        }
    }

    // 检查输入的权限码在当前的菜单项里是否包含
    private String existsPermissionCode( Long parentId, Long currentId, String _newPermissionCode ){

        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId);
        if(ObjectUtil.isNotNull(currentId))
            queryWrapper.ne(SysMenu::getId, currentId);

        List<SysMenu> sysMenus = sysMenuService.list(queryWrapper);
        if(CollectionUtil.isEmpty(sysMenus)) return ""; //  权限码相同的

        List<String> dbCode = sysMenus.stream()
                .flatMap(SysMenu-> Arrays.stream(SysMenu.getPermissionCode().split(",")))
                .collect(Collectors.toList());

        List<String> requestCode = Arrays.stream(_newPermissionCode.split(",")).collect(Collectors.toList());

        for (String code : requestCode) {
            if(dbCode.contains(code)) return code;
        }
        return "";
    }

    private void validatorMenuParams(SysMenu menu) {
        String menuType = menu.getMenuType();

        ArrayList<String> menuTypeList = CollectionUtil.newArrayList(
                EnumSysMenu.MenuType.BUTTON.getKey(),
                EnumSysMenu.MenuType.MODULE.getKey(),
                EnumSysMenu.MenuType.MENU.getKey()
        );

        if(!menuTypeList.contains(menuType))
            throw new PlantException("资源类型参数异常");

        if (menuType.equals(EnumSysMenu.MenuType.MENU.getKey())) {
            if (StringUtils.isEmpty(menu.getMenuRoute())) throw new PlantException("请输入路由地址");

        } else if (menuType.equals(EnumSysMenu.MenuType.BUTTON.getKey())) {
            if (StringUtils.isEmpty(menu.getPermissionCode())) throw new PlantException("请输入权限码");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyMenu(SysMenuParams menuParams) {
        SysMenu menu = new SysMenu();
        BeanUtil.copyProperties(menuParams,menu);

        validatorMenuParams(menu); //校验参数

        SysMenu dbMenu = sysMenuService.getById(menu.getId());
        if (ObjectUtil.isNull(dbMenu)) throw new PlantException("您修改的资源不存在");

        if(!dbMenu.getMenuType().equals(menu.getMenuType())){
            throw new PlantException("不允许修改资源类型");
        }

        if(!dbMenu.getParentId().equals(menu.getParentId())){
            throw new PlantException("不允许修改上级菜单");
        }

        if(menu.getMenuType().equals(EnumSysMenu.MenuType.BUTTON.getKey())){
            String check = existsPermissionCode(menu.getParentId(), menu.getId(), menu.getPermissionCode());
            if(StringUtils.isNotEmpty(check))
                throw new PlantException(String.format("权限码 %s 已存在~",check));
        }

        sysMenuService.updateById(menu);
    }


    @Override
    public List<MenuNodeResponse> menuNodeChild(String menuType) {

        if(StringUtils.isEmpty(menuType))
            throw new PlantException("菜单类型不允许为空");

        // 当传入类型为模块 构建模块，否则构建菜单
        String queryType = menuType.equals(EnumSysMenu.MenuType.MODULE.getKey())?EnumSysMenu.MenuType.MODULE.getKey():
                EnumSysMenu.MenuType.MENU.getKey();

        List<MenuNodeResponse> result = sysMenuService.buildMenuNode(null, queryType, menuType);

        if(!menuType.equals(EnumSysMenu.MenuType.BUTTON.getKey())) {
            MenuNodeResponse response = new MenuNodeResponse(0L, "一级菜单",EnumSysMenu.MenuType.MENU.getKey());
            response.setDisabled(
                    CollectionUtil.isNotEmpty(result) && menuType.equals(EnumSysMenu.MenuType.MODULE.getKey())
            );

            result.add(0, response);//添加到集合的第一个位置
        }

        return result;
    }


    @Override
    public SysMenu menuDetail(Long id) {
        SysMenu dbMenu = sysMenuService.getById(id);
        if(ObjectUtil.isNull(dbMenu)) throw new PlantException("您查看的菜单不存在");
        return dbMenu;
    }
}
