package com.github.boot.beans.request.sys;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
public class SysUserParams  {
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 登录账号
     */
    @NotEmpty(message = "登录账号不能为空")
    private String account;

    /**
     * 登录密码
     */
    private String password;


    /**
     * 昵称
     */
    @NotEmpty(message = "昵称不能为空")
    private String nickName;

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String phone;

    /**
     * 性别 0 女 1 男
     */
    private Integer sex;


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
