package com.seproject.account.model.email;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(value = "ACCOUNT_REGISTER_EMAIL", timeToLive = 600L)
public class AccountRegisterEmail {

    @Id
    private String email;
    private String authToken;

    private String generateAuthToken() {
        return UUID.randomUUID().toString().substring(0,8);
    }

    @Builder
    public AccountRegisterEmail(String email) {
        this.email = email;
        this.authToken = generateAuthToken();
    }

    public boolean confirm(String authToken) {
        return this.authToken.equals(authToken);
    }

    public String toMessage(){
        return "회원가입 인증번호 : " + authToken;
    }

}
