package com.seproject.admin.account.controller.dto;

import com.seproject.account.account.domain.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class AdminAccountDto {

    @Data
    @AllArgsConstructor
    @Builder(access = AccessLevel.PRIVATE)
    public static class AccountResponse {
        private Long accountId;
        private String loginId;
        private String name;
        private String nickname;
        private LocalDateTime createdAt;
        private List<String> roles;

        public AccountResponse(Long accountId,
                               String loginId,
                               String name,
                               String nickname,
                               LocalDateTime createdAt) {
            this.accountId = accountId;
            this.loginId = loginId;
            this.name = name;
            this.nickname = nickname;
            this.createdAt = createdAt;
        }

        public static AccountResponse of(Account account,List<String> roles) {

            return builder()
                    .accountId(account.getAccountId())
                    .loginId(account.getLoginId())
                    .name(account.getName())
                    .nickname(account.getNickname())
                    .createdAt(account.getCreatedAt())
                    .roles(roles)
                    .build();

        }

    }


    @Data
    public static class CreateAccountRequest {
        private String id;
        private String password;
        private String name;
        private String nickname;
        private List<String> roles;
    }

    @Data
    public static class UpdateAccountRequest {
        private String id;
        private String password;
        private String name;
        private String nickname;
        private List<String> roles;
    }

}
