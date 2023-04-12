package com.seproject.oauth2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtDecoder {

    private final String tokenPrefix;
    private final String secret; // 우리 서버만 알고 있는 비밀값


    public JwtDecoder(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.token_prefix}") String tokenPrefix
    ) {
        this.secret = secret;
        this.tokenPrefix = tokenPrefix;
    }

    public String getLoginId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String)claims.getOrDefault("email",null);
    }

    public String getProvider(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String)claims.getOrDefault("provider",null);
    }

    public String getProfile(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String)claims.getOrDefault("profile",null);
    }

    public List<String> getAuthorities(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return (List<String>) claims.getOrDefault("authorities",new ArrayList<>());
    }
}
