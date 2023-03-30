package com.seproject.seboard.dto.post;

import lombok.Data;

public class CategoryRequest {

    @Data
    public static class CreateCategoryRequest {

        private Long superCategoryId;
        private String name;
    }

    @Data
    public static class UpdateCategoryRequest {

        private Long superCategoryId;
        private String name;
    }
}
