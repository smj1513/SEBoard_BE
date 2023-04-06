package com.seproject.seboard.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class MessageResponse {
    private String message;

    public static MessageResponse of(String message) {
        return builder()
                .message(message)
                .build();
    }
}
