package com.seproject.account.model.token;

import com.seproject.account.jwt.JWTProperties;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@RedisHash(value = "LOGOUT_REFRESH_TOKEN", timeToLive = JWTProperties.REFRESH_TOKEN_EXPIRE)
public class LogoutRefreshToken {

    @Id
    private String refreshToken;
}
