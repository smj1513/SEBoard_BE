package com.seproject.account.jwt;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class JWT {

    private String accessToken;
    private String refreshToken;

    public JWT(String accessToken, String refreshToken) {
        this.accessToken = detach(accessToken);
        this.refreshToken = detach(refreshToken);
    }

    private String detach(String token) {

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return token;
    }

}
