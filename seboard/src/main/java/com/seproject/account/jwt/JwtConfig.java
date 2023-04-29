package com.seproject.account.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token_prefix}")
    private String tokenPrefix;

    @Value("${jwt.expiration_time}")
    private int expirationTime;

    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder(secret);
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(expirationTime,tokenPrefix,secret);
    }

}
