package com.seproject.account.jwt;

import com.seproject.account.model.social.AbstractOidcUser;
import com.seproject.account.model.social.KakaoOidcUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Date;

@Slf4j
public class JwtProvider {

    private final int expirationTime; // 10일 (1/1000초)
    private final String tokenPrefix;
    private final String secret;

    public JwtProvider(int expirationTime, String tokenPrefix, String secret) {
        this.expirationTime = expirationTime;
        this.tokenPrefix = tokenPrefix;
        this.secret = secret;
    }

    public String createRefreshToken(AbstractAuthenticationToken token) {
        String refreshToken = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*180))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        String result = tokenPrefix + " " +refreshToken;
        return result;
    }

    public String createJWT(AbstractAuthenticationToken token) {

        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return tokenPrefix + " " + jwt;
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
        return tokenPrefix + " " + jwt;
    }
}
