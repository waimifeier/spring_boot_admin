package com.github.boot.test;

import com.github.boot.AdminApplication;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
@Slf4j
public class JasyptTest {


    @Value("${jasypt.encryptor.password}")
    private String encryptorPassword;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Test
    public void test() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encryptorPassword);
        String encryptedDatasourceUsername = encryptor.encrypt(datasourceUsername);
        String encryptedDatasourcePassword = encryptor.encrypt(datasourcePassword);

        log.info("数据库用户名加密结果：{}", encryptedDatasourceUsername);
        log.info("数据库密码加密结果：{}", encryptedDatasourcePassword);
    }
}