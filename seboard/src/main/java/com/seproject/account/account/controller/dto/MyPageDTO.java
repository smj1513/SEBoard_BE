package com.seproject.account.account.controller.dto;

import com.seproject.account.account.domain.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class MyPageDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MyInfoResponse {
        private String nickname;
        private String email;
        private List<String> roles;

        public static MyInfoResponse toDTO(String email,String nickname,List<String> roles) {
            return builder()
                    .nickname(nickname)
                    .roles(roles)
                    .email(email)
                    .build();
        }
    }

    @Data
    public static class MyInfoChangeRequest {
        private String nickname;

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MyInfoChangeResponse {
        private String nickname;

        public static MyInfoChangeResponse toDTO(Account account) {
            return builder()
                    .nickname(account.getNickname())
                    .build();
        }
    }
}
