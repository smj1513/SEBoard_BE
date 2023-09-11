package com.seproject.account.token.controller;

import com.seproject.account.account.domain.OAuthAccount;
import com.seproject.account.social.TemporalUserInfo;
import com.seproject.account.social.repository.TemporalUserInfoRepository;
import com.seproject.account.token.domain.UserToken;
import com.seproject.account.token.domain.repository.UserTokenRepository;
import com.seproject.global.AccountSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class UserTokenControllerTest {

    @Autowired MockMvc mvc;
    @Autowired EntityManager em;
    @Autowired UserTokenRepository userTokenRepository;
    @Autowired TemporalUserInfoRepository temporalUserInfoRepository;

    @Autowired AccountSetup accountSetup;

    

    @Test
    public void UserToken_조회() throws Exception {
        OAuthAccount oAuthAccount = accountSetup.createOAuthAccount();

        String id = UUID.randomUUID().toString();
        UserToken userToken = UserToken.builder()
                .id(id)
                .oAuthAccountId(oAuthAccount.getAccountId())
                .build();

        userTokenRepository.save(userToken);

        em.flush(); em.clear();

        mvc.perform(get("/auth/kakao")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("id",id)
                .characterEncoding("UTF-8")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());

        Optional<UserToken> findUserToken = userTokenRepository.findById(id);

        assertFalse(findUserToken.isPresent());
    }

    @Test
    public void TemporalUserInfo_조회() throws Exception {

        String email = UUID.randomUUID().toString().substring(0,8) + "@gmail.com";
        String id = UUID.randomUUID().toString();

        TemporalUserInfo temporalUserInfo = TemporalUserInfo.builder()
                .id(id)
                .subject(UUID.randomUUID().toString())
                .provider("kakao")
                .email(email)
                .name("KIM")
                .nickname("NICKNAME")
                .build();

        temporalUserInfoRepository.save(temporalUserInfo);

        em.flush(); em.clear();

        mvc.perform(get("/register/oauth")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .queryParam("id",id)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(temporalUserInfo.getId()))
                .andExpect(jsonPath("subject").value(temporalUserInfo.getSubject()))
                .andExpect(jsonPath("provider").value(temporalUserInfo.getProvider()))
                .andExpect(jsonPath("email").value(temporalUserInfo.getEmail()))
                .andExpect(jsonPath("name").value(temporalUserInfo.getName()))
                .andExpect(jsonPath("nickname").value(temporalUserInfo.getNickname()));

        Optional<TemporalUserInfo> byId = temporalUserInfoRepository.findById(id);
        assertFalse(byId.isPresent());
    }
    
    
    
    
    
    
    
    
    
    

}