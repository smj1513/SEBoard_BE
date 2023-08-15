package com.seproject.admin.role.application;

import com.seproject.account.role.domain.Role;
import com.seproject.account.role.service.RoleService;
import com.seproject.admin.role.controller.dto.RoleDTO;
import com.seproject.admin.role.controller.dto.RoleDTO.CreateRoleRequest;
import com.seproject.admin.role.controller.dto.RoleDTO.DeleteRoleRequest;
import com.seproject.admin.role.controller.dto.RoleDTO.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.admin.role.controller.dto.RoleDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminRoleAppService {

    private final RoleService roleService;

    public List<RoleResponse> findAll(){
        List<Role> roles = roleService.findAll();

        return roles.stream()
                .map(RoleResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createRole(CreateRoleRequest request) {
        return roleService.createRole(request.getName(), request.getDescription(), request.getAlias());
    }

    @Transactional
    public void deleteRole(Long roleId) {
        roleService.deleteRole(roleId);
    }

    @Transactional
    public void updateRole(Long roleId, UpdateRoleRequest request) {
        //TODO
        roleService.updateRole(roleId,request.getName(),request.getDescription(),request.getAlias());
    }


}
