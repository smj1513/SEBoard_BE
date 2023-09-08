package com.seproject.account.token.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.account.account.domain.FormAccount;
import com.seproject.account.account.domain.OAuthAccount;
import com.seproject.account.role.domain.Role;
import com.seproject.account.token.controller.dto.TokenDTO.AccessTokenRefreshRequest;
import com.seproject.account.token.domain.JWT;
import com.seproject.account.token.domain.repository.LogoutLargeRefreshTokenRepository;
import com.seproject.account.token.domain.repository.LogoutRefreshTokenRepository;
import com.seproject.account.token.service.TokenService;
import com.seproject.account.token.utils.JWTProperties;
import com.seproject.account.token.utils.JwtProvider;
import com.seproject.global.AccountSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class RefreshTokenControllerTest {

    @Autowired private AccountSetup accountSetup;
    @Autowired private MockMvc mvc;
    @Autowired private TokenService tokenService;
    @Autowired private LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    @Autowired private LogoutLargeRefreshTokenRepository logoutLargeRefreshTokenRepository;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private JwtProvider jwtProvider;

    @Test
    public void 토큰_재발행() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(formAccount.getLoginId(),formAccount.getPassword(),formAccount.getAuthorities());
        JWT refreshToken = tokenService.createRefreshToken(token);
        JWT accessToken = tokenService.createAccessToken(token);
        AccessTokenRefreshRequest request = new AccessTokenRefreshRequest();
        request.setRefreshToken(refreshToken.getToken());

        ResultActions perform = mvc.perform(
                MockMvcRequestBuilders.post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty());
        //TODO : isLargeToken?
        boolean exists = logoutRefreshTokenRepository.existsById(refreshToken.getToken());
        boolean notExists = logoutLargeRefreshTokenRepository.existsById(refreshToken.getToken());
        Assertions.assertTrue(exists);
        Assertions.assertFalse(notExists);
    }

    @Test
    public void large_토큰_재발행() throws Exception {
        OAuthAccount oAuthAccount = accountSetup.createOAuthAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(oAuthAccount.getLoginId(),oAuthAccount.getPassword(),oAuthAccount.getAuthorities());
        JWT refreshToken = tokenService.createLargeRefreshToken(token);
        JWT accessToken = tokenService.createAccessToken(token);
        AccessTokenRefreshRequest request = new AccessTokenRefreshRequest();
        request.setRefreshToken(refreshToken.getToken());

        ResultActions perform = mvc.perform(
                MockMvcRequestBuilders.post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty());
        //TODO : isLargeToken?

        boolean notExists = logoutRefreshTokenRepository.existsById(refreshToken.getToken());
        boolean exists = logoutLargeRefreshTokenRepository.existsById(refreshToken.getToken());
        Assertions.assertTrue(exists);
        Assertions.assertFalse(notExists);
    }

    @Test
    public void 토큰_재발행_미등록_사용자() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(UUID.randomUUID().toString(),formAccount.getPassword(),formAccount.getAuthorities());

        JWT refreshToken = tokenService.createRefreshToken(token);
        JWT accessToken = tokenService.createAccessToken(token);
        AccessTokenRefreshRequest request = new AccessTokenRefreshRequest();
        request.setRefreshToken(refreshToken.getToken());

        ResultActions perform = mvc.perform(
                MockMvcRequestBuilders.post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void 토큰_재발행_비로그인() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(formAccount.getLoginId(),formAccount.getPassword(),formAccount.getAuthorities());

        JWT refreshToken = tokenService.createRefreshToken(token);
        AccessTokenRefreshRequest request = new AccessTokenRefreshRequest();
        request.setRefreshToken(refreshToken.getToken());

        ResultActions perform = mvc.perform(
                MockMvcRequestBuilders.post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 만료된_refresh_토큰_전달() throws Exception {
        OAuthAccount oAuthAccount = accountSetup.createOAuthAccount();
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(oAuthAccount.getLoginId(),oAuthAccount.getPassword(),oAuthAccount.getAuthorities());
        Instant instant = Instant.now().minusSeconds(30*60).truncatedTo(ChronoUnit.SECONDS);
        Instant expiredDate = instant.minus(10*60, ChronoUnit.SECONDS);

        JWT refreshToken = jwtProvider.createToken(token, JWTProperties.LARGE_REFRESH_TOKEN,JWTProperties.HS256, Date.from(instant),Date.from(expiredDate));
        JWT accessToken = tokenService.createAccessToken(token);

        AccessTokenRefreshRequest request = new AccessTokenRefreshRequest();
        request.setRefreshToken(refreshToken.getToken());

        ResultActions perform = mvc.perform(
                MockMvcRequestBuilders.post("/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                        .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}