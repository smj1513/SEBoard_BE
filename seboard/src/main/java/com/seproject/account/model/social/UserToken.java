package com.seproject.account.model.social;

import com.seproject.account.model.AccessToken;
import com.seproject.account.model.Account;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import static com.seproject.account.controller.dto.LoginDTO.*;

@Builder
@Getter
@RedisHash(value = "USER_TOKEN", timeToLive = 3600L)
public class UserToken {

    @Id
    private String id;

    private Account account;

}
