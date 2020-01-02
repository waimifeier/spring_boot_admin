package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.beans.sys.RoleInfoResponse;
import com.github.boot.dao.sys.SysMenuMapper;
import com.github.boot.model.sys.SysMenu;
import com.github.boot.service.sys.SysMenuService;
import com.github.boot.enums.sys.EnumSysMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {




    // 根据菜单的父id查询 菜单列表
    private List<SysMenu> selectSysMenuNodeByParentId(Long parentId, String menuType) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper();

        if(menuType.equals(EnumSysMenu.MenuType.MENU.getKey())){
            lambdaQueryWrapper.eq(SysMenu::getMenuType, EnumSysMenu.MenuType.MENU.getKey());
        }
        else if(menuType.equals(EnumSysMenu.MenuType.BUTTON.getKey())){
            lambdaQueryWrapper.in(SysMenu::getMenuType, CollectionUtil.newArrayList(
                    EnumSysMenu.MenuType.MENU.getKey(),
                    EnumSysMenu.MenuType.BUTTON.getKey()
            ));
        }
        else if(menuType.equals(EnumSysMenu.MenuType.MODULE.getKey())){
            lambdaQueryWrapper.eq(SysMenu::getMenuType, EnumSysMenu.MenuType.MODULE.getKey());
            if( parentId > 0 ) return new ArrayList<>();
        }

        lambdaQueryWrapper.eq(SysMenu::getParentId, parentId).orderByAsc(SysMenu::getOrderNum);

        return baseMapper.selectList(lambdaQueryWrapper);
    }
    /**
     * 构建节点数据
     *
     * @param rootNode
     * @param menuType
     * @return
     */
    @Override
    public List<MenuNodeResponse> buildMenuNode(MenuNodeResponse rootNode, String menuType, String available) {
        List<MenuNodeResponse> result = new ArrayList<>();

        List<SysMenu> sysMenusNodes = selectSysMenuNodeByParentId(ObjectUtil.isNull(rootNode) ? 0L : rootNode.getValue(), menuType);

        if (CollectionUtil.isNotEmpty(sysMenusNodes)) {
            for (SysMenu sysMenu : sysMenusNodes) {
                MenuNodeResponse response = new MenuNodeResponse(sysMenu.getId(),sysMenu.getMenuName(),sysMenu.getMenuType());
                List<String> strings = buildMenuNodeAddCode(sysMenu);
                response.setAddCode(strings.get(0));
                response.setCurrentPath(getCurrentPaths(rootNode, response)); //设置当前节点的路径
                response.setDisabled(!strings.contains(available));
                List<MenuNodeResponse> childNode = buildMenuNode(response, menuType,available); //递归调用查询是否有子节点
                if (CollectionUtil.isNotEmpty(childNode)) {
                    response.setChildren(childNode);
                }
                result.add(response);
            }
        }
        return result;
    }

    private List<String> buildMenuNodeAddCode(SysMenu sysMenu) {
        if(sysMenu.getMenuType().equals(EnumSysMenu.MenuType.BUTTON.getKey()))
            return CollectionUtil.newArrayList("");
            // 当前节点是菜单，只能添加按钮
        else if(sysMenu.getMenuType().equals(EnumSysMenu.MenuType.MENU.getKey())){
            List<SysMenu> notes = this.queryChildSysMenu(sysMenu.getId());
            if(CollectionUtil.isEmpty(notes)){
                return CollectionUtil.newArrayList(
                        EnumSysMenu.MenuType.MENU.getKey(),
                        EnumSysMenu.MenuType.BUTTON.getKey()
                );
            }
            SysMenu node = notes.get(0);
            return CollectionUtil.newArrayList(node.getMenuType());
        }else if (sysMenu.getMenuType().equals(EnumSysMenu.MenuType.MODULE.getKey())){
            return sysMenu.getParentId()==0 ? CollectionUtil.newArrayList(EnumSysMenu.MenuType.MODULE.getKey()):
                    CollectionUtil.newArrayList("");
        }
        return CollectionUtil.newArrayList("");
    }

    // 封装当前节点的路径
    private List<MenuNodeResponse.CurrentPath> getCurrentPaths(MenuNodeResponse rootNode, MenuNodeResponse response) {
        List<MenuNodeResponse.CurrentPath> currentPathList = new ArrayList<>();
        MenuNodeResponse.CurrentPath currentPath = new MenuNodeResponse.CurrentPath();
        currentPath.setId(response.getValue());
        currentPath.setMenuName(response.getLabel());
        currentPathList.add(currentPath);

        if (ObjectUtil.isNotNull(rootNode)) {
            currentPathList.addAll(0, rootNode.getCurrentPath());
        }
        return currentPathList;
    }

    @Override
    public List<RoleInfoResponse.BasicMenu> buildMenuNodeByMenuIds(Long parentId, List<Long> menuIds, Boolean needButton) {
        List<RoleInfoResponse.BasicMenu> result  = new ArrayList<>();

        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenu::getId,menuIds).eq(SysMenu::getParentId,parentId);
        if(!needButton) // 不需要 button 叶子节点
            queryWrapper.ne(SysMenu::getMenuType,EnumSysMenu.MenuType.BUTTON.getKey());

        List<SysMenu> sysMenus = baseMapper.selectList(queryWrapper);

        if(CollectionUtil.isEmpty(sysMenus)) return new ArrayList<>();

        for (SysMenu sysMenu : sysMenus) {
            RoleInfoResponse.BasicMenu basicMenu = new RoleInfoResponse.BasicMenu();
            basicMenu.setId(sysMenu.getId());
            basicMenu.setMenuName(sysMenu.getMenuName());
            List<RoleInfoResponse.BasicMenu> basicMenus = buildMenuNodeByMenuIds(sysMenu.getId(), menuIds,needButton);
            basicMenu.setChild(basicMenus);
            result.add(basicMenu);
        }
        return  result;
    }

    @Override
    public List<String> buildSingleNodePermission(Long parentId, List<Long> childIds) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId,parentId).in(SysMenu::getId,childIds)
                .eq(SysMenu::getMenuType,EnumSysMenu.MenuType.BUTTON.getKey());

        List<SysMenu> sysMenus = baseMapper.selectList(queryWrapper);

        if(CollectionUtil.isEmpty(sysMenus)) return new ArrayList<>();

        return sysMenus.stream()
                .flatMap(SysMenu-> Arrays.stream(SysMenu.getPermissionCode().split(",")))
                .collect(Collectors.toList());

    }

    @Override
    public List<SysMenu> queryChildSysMenu(Long id) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)
        );
    }
}
