package com.seproject.oauth2.controller.dto;


import lombok.Builder;
import lombok.Data;

public class RegisterDTO {

    @Data
    @Builder
    public static class OAuth2RegisterRequest {
        private String nickname;
        private String name;
    }


    @Data
    @Builder
    public static class FormRegisterRequest {

        private String id;
        private String password;

        private String nickname;
        private String name;
    }

}
