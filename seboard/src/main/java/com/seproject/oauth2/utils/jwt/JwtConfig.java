package com.seproject.oauth2.utils.jwt;

import com.seproject.oauth2.service.AccountService;
import com.seproject.oauth2.utils.jwt.aspect.JwtAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Autowired
    private AccountService accountService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token_prefix}")
    private String tokenPrefix;

    @Value("${jwt.expiration_time}")
    private int expirationTime;

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder(secret,tokenPrefix);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(accountService, expirationTime,tokenPrefix,secret);
    }

    @Bean
    public JwtAspect jwtAspect() {
        return new JwtAspect();
    }
}
