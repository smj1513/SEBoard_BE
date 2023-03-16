package com.seproject.seboard.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {

    private Long categoryId;
    private String mainCategory;
    private String subCategory;
}
