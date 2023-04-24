package com.seproject.oauth2.controller.dto;

import lombok.Data;

@Data
public class EmailConfirmRequestDTO {
    private String email;
    private String authToken;
}
