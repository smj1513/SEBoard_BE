package com.seproject.admin.controller.dto.post;

import lombok.Data;

import java.util.List;

public class AdminPostRequest {

    @Data
    public static class BulkDeletePostRequest{
        private List<Long> postIds;
    }
}
