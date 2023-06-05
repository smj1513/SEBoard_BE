package com.seproject.seboard.controller.dto.post;

import lombok.Data;

import static com.seproject.seboard.application.dto.category.CategoryCommand.*;
import static com.seproject.admin.dto.AuthorizationDTO.*;

public class CategoryRequest {

    @Data
    public static class CreateCategoryRequest {

        private Long superCategoryId;
        private String name;
        private String description;
        private String urlId;
        private String externalUrl;

        private CategoryAccessUpdateRequestElement manageOption;
        private CategoryAccessUpdateRequestElement writeOption;
        private CategoryAccessUpdateRequestElement exposeOption;
        private CategoryAccessUpdateRequestElement accessOption;

        public CategoryCreateCommand toCommand(String categoryType){
            return new CategoryCreateCommand(
                    this.getSuperCategoryId(),
                    this.getName(),
                    this.getDescription(),
                    this.getUrlId(),
                    this.getExternalUrl(),
                    categoryType,
                    manageOption,
                    writeOption,
                    exposeOption,
                    accessOption
            );
        }
    }

    @Data
    public static class UpdateCategoryRequest {

        private String name;
        private String urlId;
        private String externalUrl;

        public CategoryUpdateCommand toCommand(Long categoryId){
            return new CategoryUpdateCommand(
                    categoryId,
                    this.getName(),
                    this.getUrlId(),
                    this.getExternalUrl()
            );
        }
    }
}
