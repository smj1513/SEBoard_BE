package com.seproject.account.model.token;

import com.seproject.account.jwt.JWTProperties;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@RedisHash(value = "LOGOUT_TOKEN" , timeToLive = JWTProperties.ACCESS_TOKEN_EXPIRE)
public class LogoutToken {
    @Id
    private String accessToken;
}
