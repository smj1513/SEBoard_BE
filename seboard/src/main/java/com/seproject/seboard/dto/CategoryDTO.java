package com.seproject.seboard.dto;

import com.seproject.seboard.domain.model.Category;
import lombok.Builder;

public class CategoryDTO {

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
