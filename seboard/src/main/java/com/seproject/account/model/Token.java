package com.seproject.account.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@RedisHash(value = "JWT", timeToLive = 2_592_000L)
public class Token {

    @Id
    private String accessToken;
    private String refreshToken;
    private List<? extends GrantedAuthority> authorities;
}
