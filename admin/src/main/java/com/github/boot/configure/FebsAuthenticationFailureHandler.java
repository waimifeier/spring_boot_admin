package com.github.boot.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.boot.beans.common.JSONReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FebsAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String message;
        if (exception instanceof UsernameNotFoundException) {
            message = "用户不存在！";
        } else if (exception instanceof BadCredentialsException) {
            message = "用户名或密码错误！";
        } else if (exception instanceof LockedException) {
            message = "用户已被锁定！";
        } else if (exception instanceof DisabledException) {
            message = "用户不可用！";
        } else if (exception instanceof AccountExpiredException) {
            message = "账户已过期！";
        } /*else if (exception instanceof CredentialsExpiredException) {
            message = "用户密码已过期！";
        } else if (exception instanceof ValidateCodeException || exception instanceof FebsCredentialExcetion) {
            message = exception.getMessage();
        }*/ else {
            message = "认证失败，请联系网站管理员！";
        }
        response.setContentType("application/json;charset=utf8");
        response.getWriter().write(mapper.writeValueAsString(JSONReturn.buildFailure(message)));
    }
}