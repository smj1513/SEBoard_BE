package com.seproject.admin.dto;

import com.seproject.account.model.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDTO {

    @Data
    public static class RetrieveAllRoleRequest {
        private int page;
        private int perPage;
    }

    @Builder
    @Data
    public static class RetrieveRoleResponse {
        private Long roleId;
        private String name;

        public static RetrieveRoleResponse toDTO(Role role) {
            return builder()
                    .roleId(role.getId())
                    .name(role.getAuthority())
                    .build();
        }

    }

    @Builder
    @Data
    public static class RetrieveAllRoleResponse {
        private int total;
        private int nowPage;
        private int perPage;
        private List<RetrieveRoleResponse> roles;

        public static RetrieveAllRoleResponse toDTO(List<Role> roles, int total, int nowPage,int perPage) {
            List<RetrieveRoleResponse> roleResponses = roles.stream().map(RetrieveRoleResponse::toDTO).collect(Collectors.toList());
            return builder()
                    .total(total)
                    .nowPage(nowPage)
                    .perPage(perPage)
                    .roles(roleResponses)
                    .build();
        }
    }


    @Data
    public static class CreateRoleRequest {
        private String name;
    }

    @Builder
    @Data
    public static class CreateRoleResponse {
        private Long roleId;
        private String name;

        public static CreateRoleResponse toDTO(Role role) {
            return builder()
                    .roleId(role.getId())
                    .name(role.getAuthority())
                    .build();
        }

    }

    @Data
    public static class DeleteRoleRequest {
        private Long roleId;
    }

    @Builder
    @Data
    public static class DeleteRoleResponse {
        private Long roleId;
        private String name;

        public static DeleteRoleResponse toDTO(Role role) {
            return builder()
                    .roleId(role.getId())
                    .name(role.getAuthority())
                    .build();
        }

    }
}
