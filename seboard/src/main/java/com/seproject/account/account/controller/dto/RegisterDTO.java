package com.seproject.account.account.controller.dto;


import lombok.Builder;
import lombok.Data;

public class RegisterDTO {

    @Data
    @Builder
    public static class OAuth2RegisterRequest {
        private String subject;
        private String provider;
        private String email;
        private String password;
        private String name;
        private String nickname;
    }


    @Data
    @Builder
    public static class FormRegisterRequest {

        private String id;
        private String password;

        private String nickname;
        private String name;
    }

    @Data
    public static class ConfirmDuplicateNicknameRequest {
        private String nickname;
    }

    @Data
    @Builder
    public static class ConfirmDuplicateNicknameResponse {
        private boolean isDuplication;

        public static ConfirmDuplicateNicknameResponse toDTO(boolean isDuplication) {
            return builder()
                    .isDuplication(isDuplication)
                    .build();
        }
    }


}