package com.github.boot.configure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.boot.beans.common.JSONReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>登录失败异常处理 ,需要将异常注册到SecurityConfig登录失败处理器中</p>
 */
@Slf4j
@Component
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String message = "";
        if (e instanceof UsernameNotFoundException) {
            message = e.getMessage();
        } else if (e instanceof BadCredentialsException) {
            message = "密码错误";
        } else if (e instanceof LockedException) {
            message = "用户已被锁定！";
        } else if (e instanceof DisabledException) {
            message = "用户不可用！";
        } else if (e instanceof AccountExpiredException) {
            message = "账户已过期！";
        } else if (e instanceof CredentialsExpiredException) {
            message = "用户密码已过期！";
        }else {
            message = e.getMessage();
        }

        response.setContentType("application/json;charset=utf-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(JSONReturn.buildFailure(message)));
    }
}
