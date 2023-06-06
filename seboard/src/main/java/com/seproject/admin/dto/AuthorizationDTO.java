package com.seproject.admin.dto;

import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.admin.domain.SelectOption;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorizationDTO {

    @Data
    public static class AddRoleToCategoryRequest {
        private Long roleId;
        private Long categoryId;
    }


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class AddRoleToCategoryResponse {
        private String role;
        public static AddRoleToCategoryResponse toDTO(Role role) {
            return builder()
                    .role(role.getAuthority())
                    .build();
        }
    }

    @Data
    public static class DeleteRoleToCategoryRequest {
        private Long roleId;
        private Long categoryId;
    }


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DeleteRoleToCategoryResponse {
        private String role;
        public static DeleteRoleToCategoryResponse toDTO(Role role) {
            return builder()
                    .role(role.getAuthority())
                    .build();
        }
    }

    @Data
    public static class CategoryAccessUpdateRequest {
        private String name;
        private String description;
        private String externalUrl;
        private String urlId;
        private CategoryAccessUpdateRequestElement access;
        private CategoryAccessUpdateRequestElement write;
        private CategoryAccessUpdateRequestElement manage;
        private CategoryAccessUpdateRequestElement menuExpose;
    }

    @Data
    public static class CategoryAccessUpdateRequestElement {
        private String option;
        private List<Long> roles;
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CategoryAccessOptionResponse {
        private String name;
        private String description;
        private String externalUrl;
        private String urlId;
        private AccessResponse access;
        private MenuAuthorizationResponse write;
        private MenuAuthorizationResponse manage;
        private MenuAuthorizationResponse menuExpose;

        public static CategoryAccessOptionResponse toDTO(Menu menu,
                                                         AccessResponse access,
                                                         MenuAuthorizationResponse write,
                                                         MenuAuthorizationResponse manage,
                                                         MenuAuthorizationResponse menuExpose) {

            return builder()
                    .name(menu.getName())
                    .description(menu.getDescription())
                    .externalUrl(menu.getUrlInfo())
                    .urlId(menu.getUrlInfo())
                    .access(access)
                    .write(write)
                    .manage(manage)
                    .menuExpose(menuExpose)
                    .build();
        }


    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MenuAuthorizationResponse {
        private String option;
        private List<String> roles;

        public static MenuAuthorizationResponse toDTO(List<MenuAuthorization> menuAuthorizations) {
            List<Role> roles = menuAuthorizations.stream().map(MenuAuthorization::getRole).collect(Collectors.toList());
            SelectOption selectOption = menuAuthorizations.size() == 0 ? SelectOption.ALL : menuAuthorizations.get(0).getSelectOption();
            return builder()
                    .roles(roles.stream().map(Role::toString).collect(Collectors.toList()))
                    .option(selectOption.getName())
                    .build();
        }

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class AccessResponse {
        private String option;
        private List<String> roles;

        public static AccessResponse toDTO(List<Role> roles,SelectOption selectOption) {
            return builder()
                    .option(selectOption.name())
                    .roles(roles.stream().map(Role::toString).collect(Collectors.toList()))
                    .build();
        }
    }




}
