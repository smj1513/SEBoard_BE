package com.seproject.account.service;

import com.seproject.account.model.role.auth.AccessType;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.account.model.role.auth.CategoryManagerAuthorization;
import com.seproject.account.repository.role.auth.AdminAuthorizationRepository;
import com.seproject.account.repository.role.auth.AuthorizationRepository;
import com.seproject.account.repository.role.RoleAuthorizationRepository;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.account.repository.role.auth.CategoryAuthorizationRepository;
import com.seproject.account.repository.role.auth.CategoryManagerAuthorizationRepository;
import com.seproject.admin.dto.AuthorizationDTO;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;
    private final AdminAuthorizationRepository adminAuthorizationRepository;
    private final CategoryAuthorizationRepository categoryAuthorizationRepository;
    private final CategoryManagerAuthorizationRepository categoryManagerAuthorizationRepository;

    private final RoleAuthorizationRepository roleAuthorizationRepository;
    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getUrlRequestMap() {
        LinkedHashMap<RequestMatcher,List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
        List<Authorization> authorizations = authorizationRepository.findAllAuthorization();

        authorizations.forEach(authorization -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            List<Role> roles = authorization.getRoleAuthorizations().stream()
                    .map(RoleAuthorization::getRole)
                    .collect(Collectors.toList());
            roles.forEach(role -> {
                configAttributes.add(new SecurityConfig(role.getAuthority()));
            });
            requestMap.put(new AntPathRequestMatcher(authorization.getPath(),authorization.getMethod()),configAttributes);
        });

        return requestMap;
    }

    public List<CategoryAuthorization> findByCategory(Long categoryId) {
        return categoryAuthorizationRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public void update(Long categoryId, AuthorizationDTO.CategoryAccessUpdateRequest request) {

        // 1
        CategoryAuthorization writeCategoryAuthorization = categoryAuthorizationRepository.findByCategoryIdAndMethod(categoryId,"POST");
        updateWrite(writeCategoryAuthorization,request.getWrite().getRoles());

        // 2
        List<CategoryAuthorization> categoryAuthorizations = categoryAuthorizationRepository.findByCategoryId(categoryId);
        updateAccess(categoryAuthorizations,request.getAccess().getRoles());

        List<CategoryManagerAuthorization> categoryManagerAuthorizations = categoryManagerAuthorizationRepository.findByCategoryId(categoryId);
        updateManager(categoryManagerAuthorizations,request.getManager().getRoles());
//        updateMenu(authorizations,request.getMenu().getRoles());
    }

    private void updateAccess(List<CategoryAuthorization> authorizations, List<Long> roleIds) {
        for (CategoryAuthorization authorization : authorizations) {
            List<RoleAuthorization> roleAuthorizations = roleIds.stream()
                    .map(roleRepository :: findById)
                    .map((role) -> role.orElseThrow(()->new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null)))
                    .map((role) -> RoleAuthorization.builder()
                            .authorization(authorization)
                            .role(role)
                            .build())
                    .collect(Collectors.toList());

            roleAuthorizationRepository.deleteAllInBatch(authorization.getRoleAuthorizations());
            authorization.setRoleAuthorizations(roleAuthorizations);
            roleAuthorizationRepository.saveAll(roleAuthorizations);
        }
    }

    private void updateWrite(CategoryAuthorization authorization, List<Long> roleIds) {
        List<RoleAuthorization> roleAuthorizations = roleIds.stream()
                .map(roleRepository :: findById)
                .map((role) -> role.orElseThrow(()->new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null)))
                .map((role) -> RoleAuthorization.builder()
                        .authorization(authorization)
                        .role(role)
                        .build())
                .collect(Collectors.toList());

        roleAuthorizationRepository.deleteAllInBatch(authorization.getRoleAuthorizations());
        authorization.setRoleAuthorizations(roleAuthorizations);
        roleAuthorizationRepository.saveAll(roleAuthorizations);
    }

    private void updateManager(List<CategoryManagerAuthorization> authorizations, List<Long> roleIds) {
        for (CategoryManagerAuthorization authorization : authorizations) {
            List<RoleAuthorization> roleAuthorizations = roleIds.stream()
                    .map(roleRepository :: findById)
                    .map((role) -> role.orElseThrow(()->new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null)))
                    .map((role) -> RoleAuthorization.builder()
                            .authorization(authorization)
                            .role(role)
                            .build())
                    .collect(Collectors.toList());

            roleAuthorizationRepository.deleteAllInBatch(authorization.getRoleAuthorizations());
            authorization.setRoleAuthorizations(roleAuthorizations);
            roleAuthorizationRepository.saveAll(roleAuthorizations);
        }
    }

    private void updateMenu(List<CategoryAuthorization> authorizations, List<Long> roleIds) {

    }


}
