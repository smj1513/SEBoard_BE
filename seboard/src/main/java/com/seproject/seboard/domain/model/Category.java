package com.seproject.seboard.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Category {
    private Long categoryId;
    private Category superCategory;
    private String name;

    public void changeName(String name) {
        //TODO : validation
        this.name = name;
    }
}
