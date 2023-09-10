package com.seproject.account.token.utils;

import com.seproject.account.account.domain.FormAccount;
import com.seproject.account.token.domain.JWT;
import com.seproject.global.AccountSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JwtProviderTest {


    @Autowired JwtProvider jwtProvider;
    @Autowired JwtDecoder jwtDecoder;
    @Autowired AccountSetup accountSetup;

    @Test
    public void 토큰_생성() throws Exception {

        FormAccount formAccount = accountSetup.createFormAccount();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(formAccount.getLoginId(), "1234", formAccount.getAuthorities());
        JWT accessToken = jwtProvider.createAccessToken(token);

        String subject = jwtDecoder.getSubject(accessToken);
        Assertions.assertEquals(subject,formAccount.getLoginId());

        String s = "Bearer " + accessToken.getToken();

        JWT jwt = new JWT(s);
        assertFalse(jwt.getToken().startsWith("Bearer"));


    }
}