package com.seproject.account.jwt;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.seproject.account.model.Token;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.TokenValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

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

    public Authentication getAuthentication(Token token) {
        String jwt = token.getAccessToken();
        Claims claims = getClaims(jwt);
        List<? extends GrantedAuthority> authorities = token.getAuthorities();

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }
}
