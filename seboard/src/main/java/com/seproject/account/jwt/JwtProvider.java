package com.seproject.account.jwt;

import com.seproject.account.model.social.AbstractOidcUser;
import com.seproject.account.model.social.KakaoOidcUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Date;
import java.util.stream.Collectors;

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

    private AbstractOidcUser parseUser(OAuth2User oAuth2User) {
        AbstractOidcUser abstractOidcUser;
        if(oAuth2User instanceof KakaoOidcUser) {
            abstractOidcUser = (KakaoOidcUser)oAuth2User;
        } else {
            abstractOidcUser = null;
        }

        return abstractOidcUser;
    }

    public String createJWT(OAuth2AuthenticationToken authenticationToken) {
        AbstractOidcUser oidcUser = parseUser(authenticationToken.getPrincipal());

//        if(accountService.isExist(oidcUser.getId())) {
//            Account account = accountService.findAccountById(oidcUser.getId());
//            account.updateProfile(oidcUser.getProfile());
//            return createJWT(account);
//        }

        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.TEMPORAL_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(oidcUser.getProvider() + "_" + oidcUser.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim(JWTProperties.PROVIDER,oidcUser.getProvider())
                .claim(JWTProperties.ID,oidcUser.getId())
                .claim(JWTProperties.EMAIL,oidcUser.getEmail())
                .claim(JWTProperties.PROFILE,oidcUser.getProfile())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        String result = tokenPrefix + " " + jwt;
        return result;
    }

    public String createRefreshToken() {
        String refreshToken = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.REFRESH_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        String result = tokenPrefix + " " +refreshToken;
        return result;
    }


    public String createJWT(UsernamePasswordAuthenticationToken token) {

        String jwt = Jwts.builder()
                .setHeaderParam(JWTProperties.TYPE, JWTProperties.ACCESS_TOKEN)
                .setHeaderParam(JWTProperties.ALGORITHM, JWTProperties.HS256)
                .setSubject(token.getPrincipal().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim(JWTProperties.AUTHORITIES, token.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return tokenPrefix + " " + jwt;
    }
}
