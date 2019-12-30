package com.github.boot.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author dlj
 * @since 2019-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 资源名称
     */
    private String menuName;

    /**
     * 路由
     */
    private String menuRoute;

    /**
     * 菜单类型： CATALOG：目录  MENU ：菜单  BUTTON：按钮 ，MODULE 资源模块
     */
    private String menuType;

    /**
     * 父菜单ID，一级菜单为0
     */
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
    private String description;


}
