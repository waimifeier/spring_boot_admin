package com.github.boot.controller.sys;

import com.github.boot.annotation.LoginUser;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.sys.RoleUserMappingParams;
import com.github.boot.beans.request.sys.SysUserParams;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * 系统用户控制器
 */

@RestController
@RequestMapping("/sys_user")
public class SysUserController {


    @Autowired
    private UserService userService;

    /**
     * 0 左侧标记
     * @return
     */
    @RequestMapping("/badge")
    public JSONReturn badge(){
        return JSONReturn.buildSuccess(userService.badge());
    }

    /**
     * 1. 保存用户
     * @param sysUser
     * @param params
     * @return
     */
    @RequestMapping("/save")
    public JSONReturn saveUser(@LoginUser SysUser sysUser , @RequestBody @Validated SysUserParams params) {
        userService.saveUser(sysUser,params);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 2. 修改用户
     * @param sysUser
     * @param params
     * @return
     */
    @RequestMapping("/modify")
    public JSONReturn modifyUser(@LoginUser SysUser sysUser , @RequestBody @Validated SysUserParams params) {
        userService.modifyUser(sysUser,params);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3. 根据状态 ，昵称搜索 用户
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public JSONReturn userList( @RequestBody HashMap<String,Object> params) {

        return JSONReturn.buildSuccess(userService.userList(params));
    }

    /**
     * 4. 禁用启用账号
     * @param id
     * @return
     */
    @RequestMapping("/disabled/{userId:\\d+}")
    public JSONReturn disabledUser(@PathVariable("userId") Long id) {
        userService.disabledUser(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 5. 删除账号
     * @param id
     * @return
     */
    @RequestMapping("/remove/{userId:\\d+}")
    public JSONReturn removeUser(@PathVariable("userId") Long id) {
        userService.removeUser(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 6.关联角色
     * @param params
     * @return
     */
    @RequestMapping("/role_mapping")
    public JSONReturn roleMapping(@RequestBody @Validated RoleUserMappingParams params) {
        userService.roleMapping(params);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 7 获取角色列表
     * @return
     */
    @RequestMapping("/roleList")
    public JSONReturn roleList() {
        return JSONReturn.buildSuccess(userService.roleList());
    }


    /**
     * 7 获取角色列表
     * @return
     */
    @RequestMapping("/department")
    public JSONReturn department() {
        return JSONReturn.buildSuccess(userService.department());
    }


}
