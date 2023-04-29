package com.seproject.account.jwt;

import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.TokenValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class JwtDecoder {

    private final String secret;

    public JwtDecoder(String secret) {
        this.secret = secret;
    }

    public String getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getAccessToken(request);
    }

    public String getAccessToken(HttpServletRequest request) {
        String jwt = request.getHeader("Authorization");

        if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
            return jwt.substring(7);
        }
        return null;
    }

    private Claims getClaims(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (JwtException e) {
            throw new TokenValidateException(ErrorCode.INVALID_JWT, e);
        }
    }

    public String getLoginId(String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getEmail(String token) {
        Claims claims = getClaims(token);
        String email = (String)claims.get(JWTProperties.EMAIL);
        if(email == null) throw new NoSuchElementException();
        return email;
    }

    public String getProvider(String token){
        Claims claims = getClaims(token);
        String provider = (String)claims.get(JWTProperties.PROVIDER);
        if(provider == null) throw new NoSuchElementException();
        return provider;
    }

    public String getProfile(String token){
        Claims claims = getClaims(token);
        String profile = (String)claims.get(JWTProperties.PROFILE);
        if(profile == null) throw new NoSuchElementException();
        return profile;
    }

    public List<String> getAuthorities(String token){
        Claims claims = getClaims(token);
        List<String> authorities = (List<String>)claims.get(JWTProperties.AUTHORITIES);
        if(authorities == null) throw new NoSuchElementException();
        return authorities;
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = getClaims(jwt);
        List<String> authoritiesFromToken = (List<String>)claims.get(JWTProperties.AUTHORITIES);
        List<SimpleGrantedAuthority> authorities = authoritiesFromToken.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
