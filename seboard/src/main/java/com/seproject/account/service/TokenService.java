package com.seproject.account.service;

import com.seproject.account.model.Token;
import com.seproject.account.repository.TokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRedisRepository tokenRedisRepository;

    public Token addToken(String accessToken, String refreshToken, List<? extends GrantedAuthority> authorities) {

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authorities(authorities)
                .build();
        tokenRedisRepository.save(token);

        return token;
    }

    public Token findToken(String accessToken) {
        return tokenRedisRepository.findById(accessToken).orElse(null);
    }

    public Token deleteToken(String accessToken) {
        Token token = tokenRedisRepository.findById(accessToken).orElseThrow();
        tokenRedisRepository.delete(token);
        return token;
    }
}
