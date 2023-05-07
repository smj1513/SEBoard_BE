package com.seproject.account.controller.dto;

import lombok.Data;

public class AccountDTO {


    @Data
    public static class PasswordRequest {

        private String email;
    }
}
