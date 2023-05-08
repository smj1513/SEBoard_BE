package com.seproject.account.jwt;

import com.seproject.account.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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


    public JWT createToken(UsernamePasswordAuthenticationToken token){
        String accessToken = createJWT(token);
        String refreshToken = createRefreshToken(token);
        JWT jwt = new JWT(accessToken,refreshToken);
        return jwt;
    }

    public String createTemporalJWT(AbstractAuthenticationToken token) {
        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.TEMPORAL_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return jwt;
    }


    private String createRefreshToken(AbstractAuthenticationToken token) {
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.plus(30, ChronoUnit.SECONDS);
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
        Instant expiredDate = instant.plus(10, ChronoUnit.SECONDS);
        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(Date.from(instant))
                .setExpiration(Date.from(expiredDate))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return jwt;
    }


}
