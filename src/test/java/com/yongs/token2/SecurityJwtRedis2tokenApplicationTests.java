package com.yongs.token2;

import com.yongs.token2.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SecurityJwtRedis2tokenApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Test
    void test1(){
        String accessToken = jwtUtil.generateAccessToken("username");
        System.out.println(accessToken);
        System.out.println(jwtUtil.validateAccessToken(accessToken));
        System.out.println(jwtUtil.getUsernameFromToken(accessToken));
    }
}
