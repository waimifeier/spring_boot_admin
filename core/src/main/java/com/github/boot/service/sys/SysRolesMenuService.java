package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.model.sys.SysRolesMenu;

import java.util.List;

/**
 * <p>
 * 角色菜单管理表 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysRolesMenuService extends IService<SysRolesMenu> {


    /**
     * 根据角色id获取关联的菜单id
     * @param sysRolesId
     * @return
     */
    List<Long> queryRoleMenuId(List<Long> sysRolesId);
}
