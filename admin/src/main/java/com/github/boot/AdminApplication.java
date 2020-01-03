package com.github.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        log.info("启动成功 at {}" , LocalDateTime.now());
    }
}
