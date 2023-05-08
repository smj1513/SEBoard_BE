package com.seproject.account.model.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

@Builder
@Getter
@RedisHash(value = "REFRESH_TOKEN", timeToLive = 1_209_600L)
public class RefreshToken {
    @Id
    private String refreshToken;
}
