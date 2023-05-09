package com.seproject.seboard.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

    @Data
    @AllArgsConstructor
    public static class CreateMessage{
        private Long id;
        private String message;

        public static CreateMessage of(Long id, String message){
            return new CreateMessage(id, message);
        }
    }
}
