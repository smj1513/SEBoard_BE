package com.seproject.account.service;

import com.seproject.account.controller.dto.TokenDTO;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.AccessToken;
import com.seproject.account.model.RefreshToken;
import com.seproject.account.repository.AccessTokenRepository;
import com.seproject.account.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

import static com.seproject.account.controller.dto.TokenDTO.*;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final CustomUserDetailsService userDetailsService;

    private final JwtProvider jwtProvider;
    private final JwtDecoder jwtDecoder;

    @Transactional
    public TokenResponse addToken(String accessToken, String refreshToken, Collection<? extends GrantedAuthority> authorities) {

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

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
        String subject = jwtDecoder.getSubject(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(subject);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        UsernamePasswordAuthenticationToken newToken
                = new UsernamePasswordAuthenticationToken(user.getUsername(),"",authorities);

        deleteRefreshToken(refreshToken);
        String newAccessToken = jwtProvider.createJWT(newToken);
        String newRefreshToken = jwtProvider.createRefreshToken(newToken);

        addToken(newAccessToken,newRefreshToken,authorities);

        return AccessTokenRefreshResponse.toDTO(newAccessToken,newRefreshToken);
    }

}
