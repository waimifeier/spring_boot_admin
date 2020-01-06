package com.github.boot.beans.response.account;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 管理后端 token 数据封装
 */
@Data
public class BackendTokenBean  implements UserDetails{

    private Long id;          // sysUser 用户id
    private String account;   // 登录账号
    private String nickName;   // 用户昵称
    private String phone;     // 手机号
    private String photo;     // 头像
    private Date loginTime;   // 上次登录时间
    private Integer sex;      // 性别

    private List<String> roles;     // 角色
    private List<String> authCode;  // 权限码

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
