package com.github.boot.configure.security;

import com.information.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Resource
    private AdminAuthenticationFailureHandler adminAuthenticationFailureHandler;

    @Resource
    private AccountService accountService;

    @Resource
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Resource
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AjaxAccessDeniedHandler();
    }

    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

 /*   @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());
    }*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().cors();

        http
                .httpBasic().authenticationEntryPoint(customAuthenticationEntryPoint)
                // 请登录
        /*    .and()
                .formLogin()  // 不写的话，不提供表单登录*/
            .and()
                .authorizeRequests()
                .antMatchers("/test2").hasAnyAuthority("test2")
                .anyRequest().authenticated()
            .and()
                .logout()
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)  // 退出成功
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()); // 权限不足

        // 使用json登录
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter userAuthenticationFilter = new CustomAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        userAuthenticationFilter.setFilterProcessesUrl("/account/login");
        userAuthenticationFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);
        userAuthenticationFilter.setAuthenticationFailureHandler(adminAuthenticationFailureHandler);
        return userAuthenticationFilter;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(accountService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    /***
     *  过滤不需要验证的请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/info",
                 "/error",
                 "/**/*.css",
                "/**/*.js",
                "/**/*.html",
                "/favicon.ico"
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}