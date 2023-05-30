package com.seproject.admin.controller.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class AdminPostRequest {
    @Data
    public static class AdminPostRetrieveCondition{
        private Long categoryId;
        private String exposeOption;
        @JsonProperty("isReported")
        private Boolean isReported;
        private String searchOption;
        private String query;
    }
    @Data
    public static class BulkPostRequest {
        private List<Long> postIds;
    }
}
