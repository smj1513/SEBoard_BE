package com.seproject.admin.controller.dto;

import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.seboard.controller.dto.post.CategoryResponse;
import com.seproject.seboard.domain.model.category.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class AdminCategoryRetrieveResponse {

        private List<CategoryResponse> menus;

        public static AdminCategoryRetrieveResponse toDTO(List<CategoryResponse> categoryResponses) {
            return builder()
                    .menus(categoryResponses)
                    .build();
        }

    }
    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class SubCategoryRetrieveResponse {
        private List<SubCategoryResponseElement> subMenus;

        public static SubCategoryRetrieveResponse toDTO(List<Menu> subMenus) {
            return builder()
                    .subMenus(subMenus.stream()
                            .map(SubCategoryResponseElement::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class SubCategoryResponseElement {
        private String name;
        private Long menuId;

        private List<String> writeRole;
        private List<String> manageRole;

        public static SubCategoryResponseElement toDTO(Menu menu) {
            List<MenuAuthorization> menuAuthorizations = menu.getMenuAuthorizations();

            List<String> writeRole = new ArrayList<>();
            List<String> manageRole = new ArrayList<>();

            for (MenuAuthorization menuAuthorization : menuAuthorizations) {
                AccessOption accessOption = menuAuthorization.getAccessOption();

                if(accessOption.equals(AccessOption.WRITE)) {
                    writeRole.add(menuAuthorization.getRole().toString());
                } else if(accessOption.equals(AccessOption.MANAGE)) {
                    manageRole.add(menuAuthorization.getRole().toString());
                }

            }

            return builder()
                    .name(menu.getName())
                    .menuId(menu.getMenuId())
                    .writeRole(writeRole)
                    .manageRole(manageRole)
                    .build();
        }
    }


}
