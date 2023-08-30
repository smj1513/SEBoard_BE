package com.seproject.account.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.account.account.controller.dto.RegisterDTO.OAuth2RegisterRequest;
import com.seproject.account.account.domain.Account;
import com.seproject.account.account.domain.FormAccount;
import com.seproject.account.account.domain.OAuthAccount;
import com.seproject.account.account.domain.repository.AccountRepository;
import com.seproject.account.account.domain.repository.OAuthAccountRepository;
import com.seproject.account.account.service.AccountService;
import com.seproject.account.email.domain.AccountRegisterConfirmedEmail;
import com.seproject.account.email.domain.repository.AccountRegisterConfirmedEmailRepository;
import com.seproject.account.role.domain.Role;
import com.seproject.global.AccountSetup;
import com.seproject.global.RoleSetup;
import com.seproject.member.domain.Member;
import com.seproject.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.UUID;

import static com.seproject.account.account.controller.dto.RegisterDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class RegisterControllerTest {

    @Autowired private AccountSetup accountSetup;
    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountService accountService;
    @Autowired private OAuthAccountRepository oAuthAccountRepository;
    @Autowired private RoleSetup roleSetup;

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private EntityManager em;

    @Autowired private AccountRegisterConfirmedEmailRepository accountRegisterConfirmedEmailRepository;
    @Autowired private MemberRepository memberRepository;

    @Test
    public void OAuth_회원가입() throws Exception {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        OAuth2RegisterRequest request = new OAuth2RegisterRequest();
        request.setSubject("12345678");
        request.setProvider("kakao");
        request.setEmail(email);
        request.setPassword(UUID.randomUUID().toString());
        request.setName("name");
        request.setNickname("nickname");

        accountRegisterConfirmedEmailRepository.save(new AccountRegisterConfirmedEmail(email));

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty());

        OAuthAccount oAuthAccount = oAuthAccountRepository.findByLoginId(email).orElseThrow();
        Role roleUser = roleSetup.getRoleUser();
        Assertions.assertEquals(oAuthAccount.getAuthorities().size(),1);
        Assertions.assertTrue(oAuthAccount.getAuthorities().contains(roleUser));
        Member member = memberRepository.findByLoginId(email).orElseThrow();
        Assertions.assertEquals(member.getAccount(),oAuthAccount);
    }

    @Test
    public void OAuth_회원가입_미인증_이메일() throws Exception {
        String email = UUID.randomUUID().toString() + "@gmail.com";
        OAuth2RegisterRequest request = new OAuth2RegisterRequest();
        request.setSubject("12345678");
        request.setProvider("kakao");
        request.setEmail(email);
        request.setPassword(UUID.randomUUID().toString());
        request.setName("name");
        request.setNickname("nickname");

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void OAuth_회원가입_이미_존재하는_닉네임() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        String email = UUID.randomUUID().toString() + "@gmail.com";
        OAuth2RegisterRequest request = new OAuth2RegisterRequest();
        request.setSubject("12345678");
        request.setProvider("kakao");
        request.setEmail(email);
        request.setPassword(UUID.randomUUID().toString());
        request.setName("name");
        request.setNickname(formAccount.getNickname());

        accountRegisterConfirmedEmailRepository.save(new AccountRegisterConfirmedEmail(email));

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request))
        );

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    public void form회원가입() throws Exception {
        String email = UUID.randomUUID().toString() + "@kumoh.ac.kr";
        String password = UUID.randomUUID().toString();
        String nickname = "nickname";
        String name = "name";

        FormRegisterRequest request = new FormRegisterRequest();
        request.setId(email);
        request.setPassword(password);
        request.setNickname(nickname);
        request.setName(name);

        accountRegisterConfirmedEmailRepository.save(new AccountRegisterConfirmedEmail(email));

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/form")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)));

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty());

        Account account = accountRepository.findByLoginId(email).orElseThrow();
        Assertions.assertEquals(account.getNickname(),nickname);
        Assertions.assertEquals(account.getName(),name);
        Assertions.assertTrue(accountService.matchPassword(account,password));
    }

    @Test
    public void form회원가입_미인증_이메일() throws Exception {
        String email = UUID.randomUUID().toString() + "@kumoh.ac.kr";
        String password = UUID.randomUUID().toString();
        String nickname = "nickname";
        String name = "name";

        FormRegisterRequest request = new FormRegisterRequest();
        request.setId(email);
        request.setPassword(password);
        request.setNickname(nickname);
        request.setName(name);

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/form")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)));

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void form회원가입_이미_존재하는_닉네임() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        String email = UUID.randomUUID().toString() + "@kumoh.ac.kr";
        String password = UUID.randomUUID().toString();
        String nickname = formAccount.getNickname();
        String name = "name";

        FormRegisterRequest request = new FormRegisterRequest();
        request.setId(email);
        request.setPassword(password);
        request.setNickname(nickname);
        request.setName(name);

        accountRegisterConfirmedEmailRepository.save(new AccountRegisterConfirmedEmail(email));

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/form")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)));

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void 닉네임_중복_체크_미중복() throws Exception {
        String nickname = UUID.randomUUID().toString();
        ConfirmDuplicateNicknameRequest request = new ConfirmDuplicateNicknameRequest();
        request.setNickname(nickname);

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)));

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.duplication").value(false));
    }

    @Test
    public void 닉네임_중복_체크_중복() throws Exception {
        FormAccount formAccount = accountSetup.createFormAccount();
        String nickname = formAccount.getNickname();
        ConfirmDuplicateNicknameRequest request = new ConfirmDuplicateNicknameRequest();
        request.setNickname(nickname);

        ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/account/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)));

        perform.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.duplication").value(true));
    }








}