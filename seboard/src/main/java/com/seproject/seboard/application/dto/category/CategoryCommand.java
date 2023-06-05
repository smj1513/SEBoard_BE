package com.seproject.seboard.application.dto.category;

import com.seproject.seboard.controller.dto.post.CategoryRequest;
import com.seproject.seboard.controller.dto.post.CategoryRequest.CreateCategoryRequest;
import lombok.Builder;
import lombok.Data;

import static com.seproject.admin.dto.AuthorizationDTO.*;

public class CategoryCommand {
    @Data
    public static class CategoryCreateCommand{
        private Long superCategoryId;
        private String name;
        private String description;
        private String urlId;
        private String externalUrl;
        private String categoryType;
        private CategoryAccessUpdateRequestElement manage;
        private CategoryAccessUpdateRequestElement write;
        private CategoryAccessUpdateRequestElement expose;
        private CategoryAccessUpdateRequestElement access;

        public CategoryCreateCommand(Long superCategoryId, String name, String description, String urlId, String externalUrl, String categoryType,
                                     CategoryAccessUpdateRequestElement manage,
                                     CategoryAccessUpdateRequestElement write,
                                     CategoryAccessUpdateRequestElement expose,
                                     CategoryAccessUpdateRequestElement access){
            this.superCategoryId = superCategoryId;
            this.name = name;
            this.description = description;
            this.urlId = urlId;
            this.externalUrl = externalUrl;
            this.categoryType = categoryType;
            this.manage = manage;
            this.write = write;
            this.expose = expose;
            this.access = access;
        }
    }

    @Data
    public static class CategoryUpdateCommand{
        private Long categoryId;
        private String name;
        private String urlId;
        private String externalUrl;

        public CategoryUpdateCommand(Long categoryId, String name, String urlId, String externalUrl) {
            this.categoryId = categoryId;
            this.name = name;
            this.urlId = urlId;
            this.externalUrl = externalUrl;
        }
    }
}
