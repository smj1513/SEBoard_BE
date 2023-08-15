package com.seproject.account.account.service;

import com.seproject.account.account.domain.FormAccount;
import com.seproject.account.account.domain.OAuthAccount;
import com.seproject.account.token.domain.JWT;
import com.seproject.account.token.domain.repository.LogoutLargeRefreshTokenRepository;
import com.seproject.account.token.domain.repository.LogoutRefreshTokenRepository;
import com.seproject.account.token.domain.repository.LogoutTokenRepository;
import com.seproject.account.token.service.TokenService;
import com.seproject.global.AccountSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LogoutServiceTest {

    @Autowired private LogoutService logoutService;
    @Autowired private AccountSetup accountSetup;
    @Autowired private LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    @Autowired private LogoutLargeRefreshTokenRepository logoutLargeRefreshTokenRepository;
    @Autowired private LogoutTokenRepository logoutTokenRepository;
    @Autowired private TokenService tokenService;
    @Test
    public void 로그아웃_성공() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(formAccount.getLoginId(), UUID.randomUUID().toString(),formAccount.getAuthorities());

        JWT accessToken = tokenService.createAccessToken(token);
        JWT refreshToken = tokenService.createRefreshToken(token);

        logoutService.logout(accessToken,refreshToken);
        boolean existAccessToken = logoutTokenRepository.existsById(accessToken.getToken());
        boolean existRefreshToken = logoutRefreshTokenRepository.existsById(refreshToken.getToken());

        Assertions.assertTrue(existAccessToken);
        Assertions.assertTrue(existRefreshToken);
    }

    @Test
    public void 소셜_로그아웃_성공() throws Exception {
        OAuthAccount oAuthAccount = accountSetup.createOAuthAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(oAuthAccount.getLoginId(), UUID.randomUUID().toString(),oAuthAccount.getAuthorities());

        JWT accessToken = tokenService.createAccessToken(token);
        JWT refreshToken = tokenService.createLargeRefreshToken(token);

        logoutService.logout(accessToken,refreshToken);
        boolean existAccessToken = logoutTokenRepository.existsById(accessToken.getToken());
        boolean existRefreshToken = logoutLargeRefreshTokenRepository.existsById(refreshToken.getToken());

        Assertions.assertTrue(existAccessToken);
        Assertions.assertTrue(existRefreshToken);
    }


}