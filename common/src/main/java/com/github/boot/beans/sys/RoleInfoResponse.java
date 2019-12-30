package com.github.boot.beans.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色详情
 */
@Data
public class RoleInfoResponse implements Serializable  {

    private EditorRolesParams sysRoles;

    private List<BasicMenu> sysMenus; //管理的菜单

    private List<BasicSysUser> mappingUser; //关联的用户



    @Data
    public static class BasicMenu {
        private Long id;
        private String menuName;
        private List<String> permission;
        private List<BasicMenu> child;
    }

    @Data
    public static class EditorRolesParams {
        private Long id ;

        private String name;

        private String description; //描述

        private List<Long> menuId;

        private List<Long> checkMenuId;

        private Date createTime;
    }
}
