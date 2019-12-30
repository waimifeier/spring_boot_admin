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
 * 系统账号
 * </p>
 *
 * @author dlj
 * @since 2019-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

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
     * 登录账号
     */
    private String account;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户头像
     */

    private String photo;


    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 0 女 1 男
     */
    private Integer sex;

    /**
     * uuid唯一值
     */
    private String uuid;

    /**
     * 账号类型 0 超级管理员 1 普通操作人员
     */
    private Integer accountType;


    /**
     * 状态：0 正常，1禁用，-1 删除
     */
    private Integer state;


    /**
     * 是否订阅消息推送 1推送 0不推送
     */
    private Boolean subscribe;


    /**
     * company_id
     */
    private Long companyId;

    /**
     * 所属部门
     */
    private Long departmentId;

    /**
     * 职位id
     */
    private Long positionId;


}
