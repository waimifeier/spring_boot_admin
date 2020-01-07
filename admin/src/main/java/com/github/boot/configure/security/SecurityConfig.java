package com.github.boot.configure.security;

import com.github.boot.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
// 启用Spring Security的Web安全支持
@EnableWebSecurity
// 开启注解
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Resource
    private AccountService accountService;

    @Resource
    private CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;


    /**
     * 将用户设置在内存中
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void config(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http
            .formLogin()
            .loginPage("http://localhost:7564")
            .loginProcessingUrl("/account/login")
            .permitAll()
             // 登录成功处理器
            .successHandler(customizeAuthenticationSuccessHandler)
        .and()
            .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .httpBasic()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .logout()
            .clearAuthentication(true);
    }

}