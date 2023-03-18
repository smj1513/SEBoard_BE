package com.seproject.seboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class MessageDTO {
    @AllArgsConstructor
    @Data
    public static class ResponseMessageDTO{
        private String message;
    }
}
