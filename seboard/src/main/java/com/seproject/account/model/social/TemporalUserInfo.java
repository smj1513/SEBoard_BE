package com.seproject.account.model.social;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import static com.seproject.account.controller.dto.LoginDTO.*;

@Builder
@Getter
@RedisHash(value = "USER_INFO", timeToLive = 3600L)
public class TemporalUserInfo {

    @Id
    private String id;

    private TemporalLoginResponseDTO userInfo;

}
