package com.seproject.account.jwt;

import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.CustomUserNotFoundException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String secret;
    private final AccountRepository accountRepository;
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

    public boolean isLargeToken(String jwt) {
        try {

            if (StringUtils.hasText(jwt) && jwt.startsWith(JWTProperties.TOKEN_PREFIX)) {
                jwt = jwt.substring(7);
            }

            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getHeader()
                    .get(JWTProperties.TYPE)
                    .equals(JWTProperties.LARGE_REFRESH_TOKEN);

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

    public Authentication getAuthentication(String jwt) {
        try{
            Claims claims = getClaims(jwt);
            Object authoritiesValue = claims.get(JWTProperties.AUTHORITIES);
            if(authoritiesValue == null) throw new CustomAuthenticationException(ErrorCode.INVALID_JWT,null);

            Account account = accountRepository.findByLoginId(claims.getSubject())
                    .orElseThrow(() -> new CustomUserNotFoundException(ErrorCode.USER_NOT_FOUND,null));

            return new UsernamePasswordAuthenticationToken(account, jwt, account.getAuthorities());
        } catch (ClassCastException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_JWT,e);
        }
    }
}
