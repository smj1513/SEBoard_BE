package com.seproject.account.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@RedisHash(value = "ACCESS_TOKEN", timeToLive = 3600L)
public class AccessToken {

    @Id
    private String accessToken;
    private List<? extends GrantedAuthority> authorities;
}
