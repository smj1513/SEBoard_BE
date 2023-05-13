package com.seproject.account.controller.dto;

import com.seproject.account.model.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;


public class AccountDTO {

    @Data
    public static class PasswordRequest {

        private String email;
        private String password;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class PasswordResponse {
        private String email;

        public static PasswordResponse toDTO(Account account){
            return builder()
                    .email(account.getLoginId())
                    .build();
        }
    }
}
