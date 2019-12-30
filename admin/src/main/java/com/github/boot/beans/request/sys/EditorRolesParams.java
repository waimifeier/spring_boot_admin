package com.github.boot.beans.request.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑系统角色参数
 */
@Data
public class EditorRolesParams implements Serializable{

    private Long id ;

    @NotEmpty(message = "请填写角色名字")
    private String name;


    @NotEmpty(message = "请填写描述信息")
    private String description; //描述


    @NotNull(message = "请为该角色分配权限")
    private List<Long> menuId;

    @NotNull(message = "请为该角色分配权限")
    private List<Long> checkMenuId;

    private Date createTime;
}
