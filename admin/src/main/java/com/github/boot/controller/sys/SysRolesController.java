package com.github.boot.controller.sys;

import com.github.boot.annotation.SysLog;
import com.github.boot.beans.common.JSONReturn;
import com.github.boot.beans.request.sys.EditorRolesParams;
import com.github.boot.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


/**
 * 系统角色控制器
 */

@RestController
@RequestMapping("/sys_roles")
public class SysRolesController {

    @Autowired
    private RoleService roleService;

    /**
     * 1 查看列表
     * @param params
     * @return
     */
    @RequestMapping("/list")
    public JSONReturn rolesList(@RequestBody HashMap<String,Object> params) {
        return JSONReturn.buildSuccess(roleService.getRoleList(params));
    }

    /**
     * 2. 删除角色
     * @param id
     * @return
     */
    @RequestMapping("/remove/{id:\\d+}")
    public JSONReturn removeRolesById(@PathVariable("id") Long id) {
        roleService.removeRolesById(id);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 3. 添加角色
     * @param rolesParams
     * @return
     */
    @RequestMapping("/save")
    public JSONReturn saveRoles(@RequestBody @Validated EditorRolesParams rolesParams){
        roleService.saveRoles(rolesParams);
        return JSONReturn.buildSuccessEmptyBody();
    }

    /**
     * 4. 修改角色
     * @param rolesParams
     * @return
     */
    @RequestMapping("/modify")
    public JSONReturn modifyRoles(@RequestBody @Validated EditorRolesParams rolesParams) {
        roleService.modifyRoles(rolesParams);
        return JSONReturn.buildSuccessEmptyBody();
    }


    /**
     * 5 获取详情
     * @param id
     * @return
     */
    @RequestMapping("/info/{id:\\d+}")
    @SysLog("获取角色详情")
    public JSONReturn roleInfo(@PathVariable Long id){
        return JSONReturn.buildSuccess(roleService.roleInfo(id));
    }

    /**
     * 6. 获取菜单列表
     * @return
     */
    @RequestMapping("/menuList")
    public JSONReturn  menuList(){
        return JSONReturn.buildSuccess(roleService.menuList());
    }

}
