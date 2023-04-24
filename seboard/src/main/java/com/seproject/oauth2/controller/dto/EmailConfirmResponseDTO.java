package com.seproject.oauth2.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class EmailConfirmResponseDTO {

    private String name;
    private String email;


    public static EmailConfirmResponseDTO toDTO(String name,String email) {
        return EmailConfirmResponseDTO.builder()
                .name(name)
                .email(email)
                .build();
    }
}
