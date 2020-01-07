package com.github.boot.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author dlj
 * @since 2019-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
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
     * 角色名字
     */
    private String roleName;

    /**
     * 权限码
     */
    private String roleCode;
    /**
     * 角色备注
     */
    private String roleRemarks;

    /**
     * 是否删除 0 正常 1删除
     */
    private Boolean deleted;


    /**
     * 选择的时候叶子节点
     */
    private String checkNode;


}
