package com.github.boot.configure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.boot.beans.common.JSONReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
 
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("AT onAuthenticationSuccess(...) function!");
        WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        log.info("login--IP:"+details.getRemoteAddress());
 
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication1 = context.getAuthentication();
        Object principal = authentication1.getPrincipal();
        Object principal1 = authentication.getPrincipal();
 
        String name = authentication.getName();
        log.info("login--name:"+name+" principal:"+principal+" principal1:"+principal1);

        response.setContentType("application/json;charset=utf-8");
        String result = objectMapper.writeValueAsString(JSONReturn.buildSuccess("登录成功"));
        response.getWriter().write(result);
    }
}