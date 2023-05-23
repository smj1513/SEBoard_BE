package com.seproject.admin.dto;

import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.seboard.domain.model.category.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CategoryAuthorizationRetrieveResponse {
        private Long categoryId;
        private String urlInfo;
        private String accessType;
        private List<String> roles;

        public static CategoryAuthorizationRetrieveResponse toDTO(CategoryAuthorization categoryAuthorization) {
            Category category = categoryAuthorization.getCategory();
            List<RoleAuthorization> roleAuthorizations = categoryAuthorization.getRoleAuthorizations();
            List<String> roles = roleAuthorizations.stream().map(RoleAuthorization::getRole)
                    .map(Role::toString)
                    .collect(Collectors.toList());
            return builder()
                    .categoryId(categoryAuthorization.getId())
                    .urlInfo(category.getUrlInfo())
                    .roles(roles)
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CategoryAuthorizationRetrieveResponses {
        private List<CategoryAuthorizationRetrieveResponse> categoryAuthorizations;

        public static CategoryAuthorizationRetrieveResponses toDTO(List<CategoryAuthorization> categoryAuthorizations) {
            return builder()
                    .categoryAuthorizations(categoryAuthorizations.stream()
                            .map(CategoryAuthorizationRetrieveResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }

    }

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
        private CategoryAccessUpdateRequestElement access;
        private CategoryAccessUpdateRequestElement write;
        private CategoryAccessUpdateRequestElement manager;
        private CategoryAccessUpdateRequestElement menu;
    }

    @Data
    public static class CategoryAccessUpdateRequestElement {
        private List<Long> roles;
    }




}
