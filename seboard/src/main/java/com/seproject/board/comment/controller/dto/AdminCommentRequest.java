package com.seproject.board.comment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class AdminCommentRequest {
    @Data
    public static class AdminCommentRetrieveCondition{
        @JsonProperty("isReported")
        private Boolean isReported;
        @JsonProperty("isReadOnlyAuthor")
        private Boolean isReadOnlyAuthor;
        private String searchOption;
        private String query;
    }
    @Data
    public static class BulkCommentRequest {
        private List<Long> commentIds;
    }
}
