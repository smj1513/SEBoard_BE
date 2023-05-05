package com.seproject.account.jwt;

import com.seproject.account.model.AccessToken;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.AccessTokenExpiredException;
import com.seproject.error.exception.TokenValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            if (StringUtils.hasText(jwt) && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();

            return body;
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        catch (JwtException e) {
            throw new TokenValidateException(ErrorCode.INVALID_JWT, e);
        }
    }
    public String getSubject(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
    public Authentication getAuthentication(AccessToken accessToken) {
        String jwt = accessToken.getAccessToken();
        Claims claims = getClaims(jwt);
        Collection<? extends GrantedAuthority> authorities = accessToken.getAuthorities();

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }

    public boolean isTemporalToken(String accessToken) {
        try{
            String type = (String)Jwts.parser()
                    .setSigningKey(secret)
                    .parse(accessToken)
                    .getHeader().get(JWTProperties.TYPE);
            return type.equals(JWTProperties.TEMPORAL_TOKEN);
        }
        catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        catch (JwtException e) {
            throw new TokenValidateException(ErrorCode.INVALID_JWT, e);
        }
    }
}
