package com.github.boot.configure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan("com.github.boot.dao")
public class MybatisConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 当前配置环境为dev时候才起作用
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }
}
