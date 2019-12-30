package com.github.boot.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.boot.beans.sys.BasicSysUser;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.model.sys.SysUserRoles;

import java.util.List;

/**
 * <p>
 * 角色用户关联表(一个用户多个角色) 服务类
 * </p>
 *
 * @author dlj
 * @since 2019-07-24
 */
public interface SysUserRolesService extends IService<SysUserRoles> {

    /**
     * 根据角色id 查找有哪些用户关联了
     * @param rolesId
     * @return
     */
    List<BasicSysUser> selectUserListByRoleId(Long rolesId);


    /**
     * 根据用户id 查询关联了那些权限
     * @param userId
     * @return
     */
    List<SysRoles> selectRolesByUserId(Long userId);

    /**
     * 删除用户管理的所有角色
     * @param userId
     */
    void deleteAllRoles(Long userId);
}
