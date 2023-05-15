package com.seproject.account.controller.dto;

import com.seproject.account.model.Account;
import com.seproject.account.model.role.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


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

    @Data
    public static class KumohAuthRequest {
        private String email;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class KumohAuthResponse {
        private String email;
        private List<String> authorities;

        public static KumohAuthResponse toDTO(Account account){
            return builder()
                    .email(account.getLoginId())
                    .authorities(account.getAuthorities().stream()
                            .map(Role::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
