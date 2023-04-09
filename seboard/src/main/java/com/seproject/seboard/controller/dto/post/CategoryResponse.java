package com.seproject.seboard.controller.dto.post;


import com.seproject.seboard.domain.model.post.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class CategoryResponse {
    private String mainCategory;
    private String subCategory;

    public static CategoryResponse toDTO(Category category){
        Category superCategory = category.getSuperCategory();
        String mainCategoryName =
                superCategory != null ? superCategory.getName() : null;

        return builder()
                .mainCategory(mainCategoryName)
                .subCategory(category.getName())
                .build();
    }
}
