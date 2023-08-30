package com.seproject.admin.role.controller.dto;

import com.seproject.account.role.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RoleResponse {
        private Long roleId;
        private String name;
        private String description;
        private String alias;

        public RoleResponse(Long roleId, String name, String description, String alias) {
            this.roleId = roleId;
            this.name = name;
            this.description = description;
            this.alias = alias;
        }

        public static RoleResponse of(Role role) {
            return builder()
                    .roleId(role.getId())
                    .name(role.getAuthority())
                    .description(role.getDescription())
                    .alias(role.toString())
                    .build();
        }
    }

    @Data
    public static class CreateRoleRequest {
        private String name;
        private String description;
        private String alias;
    }

    @Data
    public static class UpdateRoleRequest {
        private String name;
        private String description;
        private String alias;
    }

    @Data
    public static class DeleteRoleRequest {
        private Long roleId;
    }

}
