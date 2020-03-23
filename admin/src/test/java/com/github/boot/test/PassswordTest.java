package com.github.boot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PassswordTest {
    @Test
    public void testPassword(){
        String hashpw = "" ;//  BCrypt.hashpw("123456", BCrypt.gensalt());
        System.out.println(hashpw);
    }
}

