package com.github.boot.beans.request.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class SysMenuParams {

    private Long id;

    /**
     * 资源名称
     */
    @NotEmpty(message = "请填写资源名称")
    private String menuName;

    /**
     * 路由
     */
    private String menuRoute;

    /**
     * 菜单类型： CATALOG：目录  MENU ：菜单  BUTTON：按钮 ，MODULE 资源模块
     */
    @NotEmpty(message = "请选择资源类型")
    private String menuType;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "请选择上级菜单")
    private Long parentId;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否隐藏 0显示 1 隐藏
     */
    private Boolean hidden;


    /**
     * 权限码
     */
    private String permissionCode;


    /**
     * 资源描述
     *
     */
    @NotEmpty(message = "请填写描述信息")
    private String description;

}
