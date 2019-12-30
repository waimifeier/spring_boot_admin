package com.github.boot.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.boot.model.sys.SysRoles;
import com.github.boot.model.sys.SysUser;
import com.github.boot.model.sys.SysUserRoles;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 * 角色用户关联表(一个用户多个角色) Mapper 接口
 * </p>
 *
 * @author dlj
 * @since 2019-07-25
 */
public interface SysUserRolesMapper extends BaseMapper<SysUserRoles> {


    /**
     *  根据用户id 获取关联的那些角色
     * @param userId
     * @return
     */
    @Select("select roles.* from sys_roles roles \n" +
            "left join sys_user_roles ur  on ur.sys_role_id = roles.id \n" +
            "where ur.deleted = 0 and roles.deleted = 0 and ur.sys_user_id = #{userId}")
    @ResultMap("com.github.boot.dao.sys.SysRolesMapper.BaseResultMap")
    List<SysRoles> selectRolesByUserId(@Param("userId") Long userId);



    /**
     *  根据角色id 获取关联的那些用户
     * @param rolesId
     * @return
     */
    @Select("select `user`.* from sys_user `user` \n" +
            "left join sys_user_roles ur  on ur.sys_user_id = `user`.id \n" +
            "where  ur.deleted = 0 and `user`.state <> -1 and ur.sys_role_id = #{rolesId}")
    @ResultMap("com.github.boot.dao.sys.SysUserMapper.BaseResultMap")
    List<SysUser> selectUserListByRoleId(@Param("rolesId") Long rolesId);
}
