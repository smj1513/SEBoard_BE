package com.seproject.account.controller.dto;

import lombok.Builder;
import lombok.Data;


public class LoginDTO {

    @Data
    @Builder
    public static class LoginResponseDTO{
        private String accessToken;
        private String refreshToken;
    }
}
