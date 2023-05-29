package com.seproject.account.controller.dto;

import com.seproject.account.model.account.Account;
import com.seproject.account.model.role.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


public class AccountDTO {

    @Data
    public static class ResetPasswordRequest {

        private String email;
        private String password;
    }

    @Builder(access = AccessLevel.PRIVATE)
    @Data
    public static class ResetPasswordResponse {
        private String email;

        public static ResetPasswordResponse toDTO(Account account){
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

    @Data
    public static class PasswordChangeRequest {
        private String nowPassword;
        private String newPassword;
    }

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
    public static class WithDrawAccountRequest {
        private String refreshToken;

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class WithDrawAccountResponse {
        private boolean requiredRedirect;
        private String url;

        private Long accountId;
        private String loginId;
        private List<String> roles;
        private String nickname;
        private String name;
        public static WithDrawAccountResponse toDTO(Account account,boolean requiredRedirect,String url) {
            return builder()
                    .requiredRedirect(requiredRedirect)
                    .url(url)
                    .accountId(account.getAccountId())
                    .loginId(account.getLoginId())
                    .roles(account.getAuthorities().stream().map(Role::toString).collect(Collectors.toList()))
                    .nickname(account.getNickname())
                    .name(account.getName())
                    .build();
        }

    }


}
