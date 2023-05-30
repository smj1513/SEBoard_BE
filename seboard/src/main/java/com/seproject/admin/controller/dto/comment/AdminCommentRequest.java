package com.seproject.admin.controller.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class AdminCommentRequest {
    @Data
    public static class AdminCommentRetrieveCondition{
        @JsonProperty("isReported")
        private Boolean isReported;
        private String searchOption;
        private String query;
    }
    @Data
    public static class BulkCommentRequest {
        private List<Long> commentIds;
    }
}
