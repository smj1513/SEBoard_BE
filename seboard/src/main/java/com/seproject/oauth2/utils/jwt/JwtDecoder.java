package com.seproject.oauth2.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.List;

public class JwtDecoder {

    private final String secret;

    public JwtDecoder(String secret) {
        this.secret = secret;
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

        return (String)claims.getOrDefault(JWTProperties.EMAIL,null);
    }

    public String getProvider(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String)claims.getOrDefault(JWTProperties.PROVIDER,null);
    }

    public String getProfile(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return (String)claims.getOrDefault(JWTProperties.PROFILE,null);
    }

    public List<String> getAuthorities(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return (List<String>) claims.getOrDefault(JWTProperties.AUTHORITIES,new ArrayList<>());
    }
}
