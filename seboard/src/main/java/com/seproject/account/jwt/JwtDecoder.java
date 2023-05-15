package com.seproject.account.jwt;

import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.seproject.account.model.Account;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    @Value("${jwt.secret}")
    private String secret;
    private final RoleRepository roleRepository;
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

    public Authentication getAuthentication(String jwt) {
        try{
            Claims claims = getClaims(jwt);
            Object authoritiesValue = claims.get(JWTProperties.AUTHORITIES);
            if(authoritiesValue == null) throw new CustomAuthenticationException(ErrorCode.INVALID_JWT,null);
            Collection<? extends GrantedAuthority> authorities = roleRepository.findByNameIn((List<String>)authoritiesValue);
            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
        } catch (ClassCastException e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_JWT,e);
        }
    }
}
