package com.seproject.admin.dto;

import com.seproject.account.model.role.Authorization;
import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAuthorizationResponse {
        private Long id;
        private String path;
        private String method;
        private int priority;
        private List<String> roles;


        public static RetrieveAuthorizationResponse toDTO(Authorization authorization) {
            return builder()
                    .id(authorization.getId())
                    .path(authorization.getPath())
                    .method(authorization.getMethod())
                    .priority(authorization.getPriority())
                    .roles(authorization.getRoleAuthorizations().stream()
                            .map(RoleAuthorization::getRole)
                            .map(Role::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAllAuthorizationResponse {
        private List<RetrieveAuthorizationResponse> authorizations;


        public static RetrieveAllAuthorizationResponse toDTO(List<Authorization> authorizations) {
            return builder()
                    .authorizations(authorizations.stream()
                            .map(RetrieveAuthorizationResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    public static class CreateAuthorizationRequest{

        private String path;
        private String method;
        private int priority;
        private String role;

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CreateAuthorizationResponse{

        private Long id;
        private String path;
        private String method;
        private int priority;
        private List<String> roles;


        public static CreateAuthorizationResponse toDTO(Authorization authorization) {
            return builder()
                    .id(authorization.getId())
                    .path(authorization.getPath())
                    .method(authorization.getMethod())
                    .priority(authorization.getPriority())
                    .roles(authorization.getRoleAuthorizations().stream()
                            .map(RoleAuthorization::getRole)
                            .map(Role::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    public static class DeleteAuthorizationRequest {

        private Long id;

    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DeleteAuthorizationResponse{

        private Long id;
        private String path;
        private String method;
        private int priority;
        private List<String> roles;


        public static DeleteAuthorizationResponse toDTO(Authorization authorization) {
            return builder()
                    .id(authorization.getId())
                    .path(authorization.getPath())
                    .method(authorization.getMethod())
                    .priority(authorization.getPriority())
                    .roles(authorization.getRoleAuthorizations().stream()
                            .map(RoleAuthorization::getRole)
                            .map(Role::getAuthority)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
