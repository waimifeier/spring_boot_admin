package com.github.boot.service.account;


import com.github.boot.beans.request.account.ModifyUserPasswordParams;
import com.github.boot.beans.request.account.UserLoginParams;
import com.github.boot.model.sys.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;

public interface AccountService extends UserDetailsService {

    Object login(UserLoginParams params);

    void modify(SysUser sysUser, ModifyUserPasswordParams params);

    void modifyPhoto(SysUser sysUser, HashMap<String, Object> params);

    Object resourceInfo(SysUser sysUser);
}
