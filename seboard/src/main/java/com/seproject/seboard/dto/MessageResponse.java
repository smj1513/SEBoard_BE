package com.seproject.seboard.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MessageResponse {
    private String message;

    public static MessageResponse toDTO(String message) {
        return builder()
                .message(message)
                .build();
    }
}
