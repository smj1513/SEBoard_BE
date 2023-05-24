package com.seproject.admin.service;

import com.seproject.account.model.role.Role;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.admin.controller.MenuAccessOption;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.seproject.admin.dto.RoleDTO.*;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RetrieveAllRoleResponse findAll(int page, int perPage) {
        try {
            Pageable pageable = PageRequest.of(page,perPage);

            Page<Role> all = roleRepository.findAll(pageable);
            List<Role> roles = all.toList();
            int total = all.getTotalPages();
            int nowPage = all.getNumber();

            return RetrieveAllRoleResponse.toDTO(roles,total,nowPage,perPage);
        } catch (IllegalArgumentException e) {
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_PAGINATION,e);
        }
    }

    public CreateRoleResponse createRole(CreateRoleRequest createRoleRequest) {
        String roleName = createRoleRequest.getName();

        if(roleRepository.existsByName(roleName)){
            throw new CustomIllegalArgumentException(ErrorCode.ALREADY_EXIST_ROLE,null);
        }

        Role role = Role.builder()
                .name(roleName)
                .description(createRoleRequest.getDescription())
                .alias(createRoleRequest.getAlias())
                .build();

        Role savedRole = roleRepository.save(role);
        return CreateRoleResponse.toDTO(savedRole);
    }

    public DeleteRoleResponse deleteRole(Long roleId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));

        if(role.isImmutable()) {
            throw new CustomIllegalArgumentException(ErrorCode.IMMUTABLE_ROLE,null);
        }

        roleRepository.delete(role);

        return DeleteRoleResponse.toDTO(role);
    }

    @Transactional
    public UpdateRoleResponse updateRole(Long roleId,UpdateRoleRequest updateRoleRequest) {
        Role role = roleRepository.findById(roleId).orElseThrow(() ->
            new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null)
        );
        String roleName = updateRoleRequest.getName();

        if(role.isImmutable() && !role.getAuthority().equals(roleName)) {
            throw new CustomIllegalArgumentException(ErrorCode.IMMUTABLE_ROLE,null);
        }

        role.update(roleName,updateRoleRequest.getDescription(),updateRoleRequest.getAlias());
        return UpdateRoleResponse.toDTO(role);
    }


    public List<Role> convertRoles(MenuAccessOption menuAccessOption) {
        List<String> roleNames;
        switch (menuAccessOption){

            case OVER_USER: {
                roleNames = List.of(Role.ROLE_USER,Role.ROLE_KUMOH,Role.ROLE_ADMIN);
                break;
            }
            case OVER_KUMOH: {
                roleNames = List.of(Role.ROLE_KUMOH,Role.ROLE_ADMIN);
                break;
            }
            case ONLY_ADMIN: {
                roleNames = List.of(Role.ROLE_ADMIN);
                break;
            }
            default: roleNames = List.of();
        }
        return roleRepository.findByNameIn(roleNames);
    }

}
