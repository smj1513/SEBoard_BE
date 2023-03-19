package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Category;
import jdk.jfr.Unsigned;
import lombok.Builder;
import lombok.Data;

public class CategoryDTO {

    @Builder
    @Data
    public static class CategoryRequestDTO{
        private Long superCategoryId;
        private String name;
    }

    @Builder
    public static class CategoryResponseDTO {

        private String mainCategory;
        private String subCategory;

        public static CategoryResponseDTO toDTO(Category category) {
            return builder()
                    .mainCategory(category.getSuperCategory().getName())
                    .subCategory(category.getName())
                    .build();
        }

    }
}
