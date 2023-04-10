package com.seproject.oauth2.controller.dto;


import lombok.Builder;
import lombok.Data;

public class RegisterDTO {

    @Data
    @Builder
    public static class OAuth2RegisterRequest {
        private String nickname;
    }

}
