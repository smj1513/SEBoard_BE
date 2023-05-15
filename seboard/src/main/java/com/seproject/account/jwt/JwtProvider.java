package com.seproject.account.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token_prefix}")
    private String tokenPrefix;

    @Value("${jwt.expiration_time}")
    private int expirationTime;

    public JWT createLargeToken(UsernamePasswordAuthenticationToken token){
        String accessToken = createJWT(token);
        String refreshToken = createLargeRefreshToken(token);
        JWT jwt = new JWT(accessToken,refreshToken);
        return jwt;
    }

    public JWT createToken(UsernamePasswordAuthenticationToken token){
        String accessToken = createJWT(token);
        String refreshToken = createRefreshToken(token);
        JWT jwt = new JWT(accessToken,refreshToken);
        return jwt;
    }

    public String createTemporalJWT(AbstractAuthenticationToken token) {
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(30, ChronoUnit.MINUTES);
        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.TEMPORAL_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return jwt;
    }

    private String createLargeRefreshToken(AbstractAuthenticationToken token) {
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(JWTProperties.LARGE_REFRESH_TOKEN_EXPIRE, ChronoUnit.SECONDS);
        String refreshToken = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.LARGE_REFRESH_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return refreshToken;
    }

    private String createRefreshToken(AbstractAuthenticationToken token) {
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(JWTProperties.REFRESH_TOKEN_EXPIRE, ChronoUnit.SECONDS);
        String refreshToken = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return refreshToken;
    }

    private String createJWT(AbstractAuthenticationToken token) {
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(JWTProperties.ACCESS_TOKEN_EXPIRE, ChronoUnit.SECONDS);
        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .claim(JWTProperties.AUTHORITIES,token.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return jwt;
    }


}
