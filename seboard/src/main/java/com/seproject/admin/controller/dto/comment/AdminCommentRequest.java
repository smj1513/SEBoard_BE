package com.seproject.admin.controller.dto.comment;

import lombok.Data;

import java.util.List;

public class AdminCommentRequest {
    @Data
    public static class BulkDeleteCommentRequest{
        private List<Long> commentIds;
    }
}
