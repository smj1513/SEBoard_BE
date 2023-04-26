package com.seproject.seboard.controller.dto.post;


import lombok.Data;

@Data
public class CategoryResponse {
    private Long categoryId;
    private String name;

    public CategoryResponse(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
