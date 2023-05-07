package com.seproject.account.service;

import com.seproject.account.jwt.JWT;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.token.AccessToken;
import com.seproject.account.model.Account;
import com.seproject.account.model.token.RefreshToken;
import com.seproject.account.repository.token.AccessTokenRepository;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.token.RefreshTokenRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

import static com.seproject.account.controller.dto.TokenDTO.*;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;
    private final JwtDecoder jwtDecoder;

    @Transactional
    public TokenResponse addToken(JWT jwt, Collection<? extends GrantedAuthority> authorities) {
        String accessToken = jwt.getAccessToken();
        String refreshToken = jwt.getRefreshToken();

        AccessToken accessTkn = AccessToken.builder()
                .accessToken(accessToken)
                .authorities(authorities)
                .build();

        RefreshToken refreshTkn = RefreshToken.builder()
                .refreshToken(refreshToken)
                .build();

        accessTokenRepository.save(accessTkn);
        refreshTokenRepository.save(refreshTkn);

        return TokenResponse.toDTO(accessTkn,refreshTkn);
    }

    public AccessToken findAccessToken(String accessToken) {
        return accessTokenRepository.findById(accessToken).orElse(null);
    }

    public AccessToken deleteAccessToken(String accessToken) {
        AccessToken token = accessTokenRepository.findById(accessToken).orElseThrow();
        accessTokenRepository.delete(token);
        return token;
    }

    public RefreshToken findRefreshToken(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken).orElse(null);
    }

    public RefreshToken deleteRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken).orElseThrow();
        refreshTokenRepository.delete(token);
        return token;
    }

    @Transactional
    public AccessTokenRefreshResponse refresh(String refreshToken){

        JWT jwt = new JWT("empty",refreshToken);
        refreshToken = jwt.getRefreshToken();

        RefreshToken findRefreshToken = findRefreshToken(refreshToken);

        if(findRefreshToken == null) throw new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND,null);

        deleteRefreshToken(refreshToken);

        String subject = jwtDecoder.getSubject(refreshToken);
        Account account = accountRepository.findByLoginId(subject);

        Collection<? extends GrantedAuthority> authorities = account.getAuthorities();
        UsernamePasswordAuthenticationToken newToken
                = new UsernamePasswordAuthenticationToken(subject,"",authorities);
        JWT token = jwtProvider.createToken(newToken);

        String newAccessToken = token.getAccessToken();
        String newRefreshToken = token.getRefreshToken();

        addToken(token,authorities);

        return AccessTokenRefreshResponse.toDTO(newAccessToken,newRefreshToken);
    }

    public JWT createToken(UsernamePasswordAuthenticationToken token){
        JWT jwt = jwtProvider.createToken(token);
        addToken(jwt,token.getAuthorities());
        return jwt;
    }

    public JWT createToken(UserDetails userDetails){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"",userDetails.getAuthorities());
        return createToken(token);
    }

    public String createTemporalJWT(AbstractAuthenticationToken token) {
        return jwtProvider.createTemporalJWT(token);
    }

}
