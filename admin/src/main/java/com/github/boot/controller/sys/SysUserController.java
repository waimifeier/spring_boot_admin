package com.github.boot.controller.sys;

import com.github.boot.annotation.LoginUser;
import com.github.boot.annotation.SysLog;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.sys.RoleUserMappingParams;
import com.github.boot.beans.request.sys.SysUserParams;
import com.github.boot.model.sys.SysUser;
import com.github.boot.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;


/**
 * 系统用户控制器
 */

@RestController
@RequestMapping("/sys_user")
public class SysUserController {


    @Resource
    private UserService userService;

    /**
     * 0 左侧标记
     * @return
     */
    @PostMapping("/badge")
    public JSONReturn badge(){
        return JSONReturn.buildSuccess(userService.badge());
    }

    /**
     * 1. 保存用户
     * @param sysUser
     * @param params
     * @return
     */
    @PostMapping("/save")
    @SysLog("保存用户")
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
    @PostMapping("/modify")
    @SysLog("修改用户")
    public JSONReturn modifyUser(@LoginUser SysUser sysUser , @RequestBody @Validated SysUserParams params) {
        userService.modifyUser(sysUser,params);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 3. 根据状态 ，昵称搜索 用户
     * @param params
     * @return
     */
    @PostMapping("/list")
    @SysLog("获取用户列表")
    public JSONReturn userList( @RequestBody HashMap<String,Object> params) {

        return JSONReturn.buildSuccess(userService.userList(params));
    }

    /**
     * 4. 禁用启用账号
     * @param id
     * @return
     */
    @PostMapping("/disabled/{userId:\\d+}")
    @SysLog("禁用启用用户")
    public JSONReturn disabledUser(@PathVariable("userId") Long id) {
        userService.disabledUser(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 5. 删除账号
     * @param id
     * @return
     */
    @PostMapping("/remove/{userId:\\d+}")
    @SysLog("删除用户")
    public JSONReturn removeUser(@PathVariable("userId") Long id) {
        userService.removeUser(id);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 6.关联角色
     * @param params
     * @return
     */
    @PostMapping("/role_mapping")
    @SysLog("分配角色")
    public JSONReturn roleMapping(@RequestBody @Validated RoleUserMappingParams params) {
        userService.roleMapping(params);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 7 获取角色列表
     * @return
     */
    @PostMapping("/roleList")
    public JSONReturn roleList() {
        return JSONReturn.buildSuccess(userService.roleList());
    }


    /**
     * 7 获取角色列表
     * @return
     */
    @PostMapping("/department")
    public JSONReturn department() {
        return JSONReturn.buildSuccess(userService.department());
    }


}
