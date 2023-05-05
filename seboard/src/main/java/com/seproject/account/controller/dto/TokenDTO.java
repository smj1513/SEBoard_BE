package com.seproject.account.controller.dto;

import com.seproject.account.model.AccessToken;
import com.seproject.account.model.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

public class TokenDTO {


    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class TokenResponse {
        private AccessToken accessToken;
        private RefreshToken refreshToken;

        public static TokenResponse toDTO(AccessToken accessToken,RefreshToken refreshToken) {
            return builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Data
    public static class AccessTokenRefreshRequest {
        private String refreshToken;
    }

    @Builder
    @Data
    public static class AccessTokenRefreshResponse {
        private String accessToken;
        private String refreshToken;

        public static AccessTokenRefreshResponse toDTO(String accessToken,String refreshToken) {
            return builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
