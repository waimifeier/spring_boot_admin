package com.github.boot.beans.response.account;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 管理后端 token 数据封装
 */
@Data
public class BackendTokenBean {

    private Long id;          // sysUser 用户id
    private String account;   // 登录账号
    private String nickName;   // 用户昵称
    private String phone;     // 手机号
    private String photo;     // 头像
    private Date loginTime;   // 上次登录时间
    private Integer sex;      // 性别

    private List<String> roles;     // 角色
    private List<String> authCode;  // 权限码

}
