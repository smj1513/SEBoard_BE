package com.seproject.account.jwt;

import com.seproject.account.model.token.AccessToken;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String secret;

    public String getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return getAccessToken(request);
    }

    public String getAccessToken(HttpServletRequest request) {
        String jwt = request.getHeader(JWTProperties.HEADER_NAME);

        if (StringUtils.hasText(jwt) && jwt.startsWith(JWTProperties.TOKEN_PREFIX)) {
            return jwt.substring(7);
        }

        return jwt;
    }

    private Claims getClaims(String jwt) {
        try {

            if (StringUtils.hasText(jwt) && jwt.startsWith(JWTProperties.TOKEN_PREFIX)) {
                jwt = jwt.substring(7);
            }

            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();

            return body;
        } catch (ExpiredJwtException e) {
            throw new CustomAuthenticationException(ErrorCode.TOKEN_EXPIRED,e);
        }
        catch (JwtException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_JWT, e);
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

            if (StringUtils.hasText(accessToken) && accessToken.startsWith(JWTProperties.TOKEN_PREFIX)) {
                accessToken = accessToken.substring(7);
            }

            String type = (String)Jwts.parser()
                    .setSigningKey(secret)
                    .parse(accessToken)
                    .getHeader().get(JWTProperties.TYPE);
            return type.equals(JWTProperties.TEMPORAL_TOKEN);
        }
        catch (ExpiredJwtException e) {
            throw new CustomAuthenticationException(ErrorCode.TOKEN_EXPIRED,e);
        }
        catch (JwtException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_JWT, e);
        }
    }
}
