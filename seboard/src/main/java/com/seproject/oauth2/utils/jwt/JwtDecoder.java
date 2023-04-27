package com.seproject.oauth2.utils.jwt;

import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.TokenValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import java.util.List;
import java.util.NoSuchElementException;

public class JwtDecoder {

    private final String secret;

    public JwtDecoder(String secret) {
        this.secret = secret;
    }

    public void validate(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt);
        } catch (JwtException e) {
            throw new TokenValidateException(ErrorCode.INVALID_JWT, e);
        }
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
        String email = (String)claims.get(JWTProperties.EMAIL);
        if(email == null) throw new NoSuchElementException();
        return email;
    }

    public String getProvider(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        String provider = (String)claims.get(JWTProperties.PROVIDER);
        if(provider == null) throw new NoSuchElementException();
        return provider;
    }

    public String getProfile(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        String profile = (String)claims.get(JWTProperties.PROFILE);
        if(profile == null) throw new NoSuchElementException();
        return profile;
    }

    public List<String> getAuthorities(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        List<String> authorities = (List<String>)claims.get(JWTProperties.AUTHORITIES);
        if(authorities == null) throw new NoSuchElementException();
        return authorities;
    }
}
