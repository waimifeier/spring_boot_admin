package com.github.boot.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.boot.beans.common.PageInfo;
import com.github.boot.beans.common.PlantException;
import com.github.boot.beans.common.QueryPage;
import com.github.boot.beans.request.sys.EditorRolesParams;
import com.github.boot.beans.sys.MenuNodeResponse;
import com.github.boot.beans.sys.RoleInfoResponse;
import com.github.boot.enums.sys.EnumSysMenu;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.model.sys.SysRolesMenu;
import com.github.boot.service.sys.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {


    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRolesMenuService sysRolesMenuService;

    @Resource
    private SysUserRolesService sysUserRolesService;

    @Resource
    private SysRolesService sysRolesService;


    @Override
    public PageInfo getRoleList(HashMap<String, Object> params) {

        LambdaQueryWrapper<SysRoles> sysRolesQueryWrapper = new LambdaQueryWrapper<>();
        sysRolesQueryWrapper.orderByDesc(SysRoles::getCreateTime);

        String roleName = MapUtil.getStr(params, "roleName");
        if (StringUtils.isNotEmpty(roleName))
            sysRolesQueryWrapper.like(SysRoles::getRoleName, roleName);

        return new PageInfo(sysRolesService.page(QueryPage.getInstance(params), sysRolesQueryWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRolesById(Long id) {
        sysRolesService.removeById(id);
    }


    @Override
    public RoleInfoResponse roleInfo(Long id) {
        SysRoles sysRoles = sysRolesService.getById(id);
        if (ObjectUtil.isNull(sysRoles)) throw new PlantException("不存在该数据~");

        RoleInfoResponse result = new RoleInfoResponse();

        RoleInfoResponse.EditorRolesParams rolesParams = new RoleInfoResponse.EditorRolesParams();
        rolesParams.setDescription(sysRoles.getRoleRemarks());
        rolesParams.setId(sysRoles.getId());
        rolesParams.setName(sysRoles.getRoleName());
        rolesParams.setCreateTime(sysRoles.getCreateTime());

        List<Long> checkMenuId = Arrays.asList(sysRoles.getCheckNode()
                .split(",")).stream().map(Long::parseLong).collect(Collectors.toList());

        rolesParams.setMenuId(checkMenuId);
        result.setSysRoles(rolesParams);

        result.setSysMenus(selectMenuListByRoleId(sysRoles.getId()));

        result.setMappingUser(sysUserRolesService.selectUserListByRoleId(sysRoles.getId()));
        return result;

    }

    private List<RoleInfoResponse.BasicMenu> selectMenuListByRoleId(Long rolesId) {
        List<Long> menuId = sysRolesMenuService.queryRoleMenuId(Arrays.asList(rolesId));
        if(CollectionUtil.isEmpty(menuId)) return new ArrayList<>();
        return sysMenuService.buildMenuNodeByMenuIds(0L,menuId,true);
    }


    @Override
    public  List<MenuNodeResponse> menuList() {
        return sysMenuService.buildMenuNode(
                null,
                EnumSysMenu.MenuType.ALL.getKey(),
                EnumSysMenu.MenuType.ALL.getKey()
        );
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoles(EditorRolesParams rolesParams) {
        SysRoles sysRoles = new SysRoles();

        checkedRoleNameIsExists(rolesParams.getName());

        String checkNodeStr = rolesParams.getCheckMenuId()
                                        .stream()
                                        .map(String::valueOf).collect(Collectors.joining(","));

        sysRoles.setRoleName(rolesParams.getName())
                .setRoleRemarks(rolesParams.getDescription())
                .setDeleted(false)
                .setCreateUser(1L)
                .setCreateTime(new Date())
                .setCheckNode(checkNodeStr);

        sysRolesService.save(sysRoles); //保存角色

        //关联菜单和角色
        batchSaveRolesMenu(rolesParams.getMenuId(), sysRoles.getId());
    }

    private void checkedRoleNameIsExists(String roleName) {
        Integer count = sysRolesService.count(new LambdaQueryWrapper<SysRoles>().eq(SysRoles::getRoleName, roleName));
        if(count>0) throw new PlantException("已存在["+roleName+"]这个角色名称,换一个试试 ?");
    }

    /**
     * 批量关联上菜单和角色
     * @param menuId 菜单id
     * @param sysRolesId 角色id
     */
    private void batchSaveRolesMenu(List<Long> menuId, Long sysRolesId) {

        List<SysRolesMenu> sysRolesMenu = menuId.stream().distinct().map(mid -> {

            SysRolesMenu rolesMenu = new SysRolesMenu();
            rolesMenu.setCreateTime(new Date())
                    .setCreateUser(0L)
                    .setDeleted(false)
                    .setSysMenuId(mid)
                    .setSysRoleId(sysRolesId);

            return rolesMenu;
        }).collect(Collectors.toList());

        sysRolesMenuService.saveBatch(sysRolesMenu);  //保存关联的菜单
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoles(EditorRolesParams rolesParams) {

        SysRoles sysRoles = sysRolesService.getById(rolesParams.getId());
        if (ObjectUtil.isNull(sysRoles)) throw new PlantException("不存在该数据~");

        if(!sysRoles.getRoleName().equals(rolesParams.getName())){
            checkedRoleNameIsExists(rolesParams.getName());
        }

        String checkNodeStr = rolesParams.getCheckMenuId().stream()
                                                        .map(String::valueOf).collect(Collectors.joining(","));
        sysRoles.setRoleRemarks(rolesParams.getDescription())
                .setRoleName(rolesParams.getName())
                .setCheckNode(checkNodeStr);

        Boolean rc = sysRolesService.updateById(sysRoles);

        if(rc) BindSysRoleAndMenu(sysRoles,rolesParams.getMenuId());

        else throw new PlantException("修改失败！");
    }

    /**
     * 绑定菜单和角色的关联
     * @param sysRoles 角色
     * @param reqMid   绑定的菜单id
     */
    private void BindSysRoleAndMenu(SysRoles sysRoles, List<Long> reqMid) {

        // 获取数据库关联的菜单id
        List<Long> dbRolesMenuId = sysRolesMenuService.queryRoleMenuId(Arrays.asList(sysRoles.getId()));
        // 本次修改的菜单id
        List<Long> reqMenuId = reqMid;

        // 定义临时变量复制一份值
        List<Long> tempDbMenuId = CollectionUtil.newArrayList(dbRolesMenuId),
                tempReqMenuId = CollectionUtil.newArrayList(reqMenuId);

        dbRolesMenuId.removeAll(reqMenuId); //  取交集，获取本次删除了的数据

        tempReqMenuId.removeAll(tempDbMenuId); //  取交集，获取本次添加的菜单数据

        // 删除本次删除的菜单关联
        if (CollectionUtil.isNotEmpty(dbRolesMenuId)) {
            batchUpdateRolesMenu(dbRolesMenuId, sysRoles.getId());
        }

        // 批量添加菜单和角色的关联
        if (CollectionUtil.isNotEmpty(tempReqMenuId)) {
            batchSaveRolesMenu(tempReqMenuId, sysRoles.getId());
        }
    }

    /**
     * 解除菜单和角色的关联
     *
     * @param menuId  菜单id
     * @param sysRolesId 角色id
     * @return
     */
    private boolean batchUpdateRolesMenu(List<Long> menuId, Long sysRolesId) {

        // 根据指定条件更新数据
        LambdaQueryWrapper<SysRolesMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRolesMenu::getSysMenuId, menuId).eq(SysRolesMenu::getSysRoleId, sysRolesId);

        return sysRolesMenuService.remove(queryWrapper);
    }
}
