package com.seproject.account.service;

import com.seproject.account.model.role.auth.AccessType;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.account.repository.role.auth.AdminAuthorizationRepository;
import com.seproject.account.repository.role.auth.AuthorizationRepository;
import com.seproject.account.repository.role.RoleAuthorizationRepository;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.account.repository.role.auth.CategoryAuthorizationRepository;
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
    public Role addReadabilityToCategory(Long roleId, Long categoryId) {

        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));
        CategoryAuthorization categoryAuthorization = categoryAuthorizationRepository.findByCategoryIdAndMethod(categoryId, "GET");

        List<RoleAuthorization> roleAuthorizations = categoryAuthorization.getRoleAuthorizations();
        boolean flag = false;
        for (RoleAuthorization roleAuthorization : roleAuthorizations) {
            flag |= roleAuthorization.getRole().equals(role);
        }

        if(flag) {
            throw new CustomIllegalArgumentException(ErrorCode.ALREADY_EXIST_ROLE,null);
        }

        RoleAuthorization roleAuthorization = RoleAuthorization.builder()
                .authorization(categoryAuthorization)
                .role(role)
                .build();

        roleAuthorizationRepository.save(roleAuthorization);
        roleAuthorizations.add(roleAuthorization);

        return role;
    }

    @Transactional
    public Role deleteReadabilityToCategory(Long roleId, Long categoryId) {

        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));
        CategoryAuthorization categoryAuthorization = categoryAuthorizationRepository.findByCategoryIdAndMethod(categoryId, "GET");

        List<RoleAuthorization> roleAuthorizations = categoryAuthorization.getRoleAuthorizations();
        for (RoleAuthorization roleAuthorization : roleAuthorizations) {
            if(roleAuthorization.getRole().equals(role)) {
                roleAuthorizations.remove(roleAuthorization);
                roleAuthorizationRepository.delete(roleAuthorization);
                return role;
            }
        }

        throw new CustomIllegalArgumentException(ErrorCode.NOT_ENROLLED_ROLE,null);
    }

    @Transactional
    public Role addWritabilityToCategory(Long roleId, Long categoryId) {

        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));

        List<CategoryAuthorization> writableCategoryAuthorizations = categoryAuthorizationRepository.findByCategoryIdAndAccessType(categoryId);
        writableCategoryAuthorizations.forEach(categoryAuthorization -> {
            List<RoleAuthorization> roleAuthorizations = categoryAuthorization.getRoleAuthorizations();
            boolean flag = false;
            for (RoleAuthorization roleAuthorization : roleAuthorizations) {
                flag |= roleAuthorization.getRole().equals(role);
            }

            if(!flag) {
               RoleAuthorization roleAuthorization = RoleAuthorization.builder()
                       .role(role)
                       .authorization(categoryAuthorization)
                       .build();

               roleAuthorizations.add(roleAuthorization);
               roleAuthorizationRepository.save(roleAuthorization);
            }
        });
        return role;
    }


    @Transactional
    public Role deleteWritabilityToCategory(Long roleId, Long categoryId) {

        Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));
        List<CategoryAuthorization> writableCategoryAuthorizations = categoryAuthorizationRepository.findByCategoryIdAndAccessType(categoryId);


        writableCategoryAuthorizations.forEach(categoryAuthorization -> {
            List<RoleAuthorization> removes = new ArrayList<>();
            List<RoleAuthorization> roleAuthorizations = categoryAuthorization.getRoleAuthorizations();
            for (RoleAuthorization roleAuthorization : roleAuthorizations) {
                if(roleAuthorization.getRole().equals(role)) {
                    removes.add(roleAuthorization);
                }
            }
            roleAuthorizations.removeAll(removes);
            roleAuthorizationRepository.deleteAll(removes);
        });

//            if(removes.size() != 3) {
//                throw new CustomIllegalArgumentException(ErrorCode.NOT_ENROLLED_ROLE,null);
//            }



        return role;
    }


//
//    @Transactional
//    public CreateAuthorizationResponse addAccessAuthorization(Long menuId, Long roleId) {
//        Role role = roleRepository.findById(roleId).orElseThrow(
//                () -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND, null));
//        Menu menu = menuRepository.findById(menuId).orElseThrow(
//                () -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY,null));
//
//        String method = "GET";
//        String path = "/category/" + menu.getUrlInfo() + "/posts/**";
//
//        Authorization authorization;
//        if(authorizationRepository.existsByPathAndMethod(path, method)) {
//            authorization = authorizationRepository.findAuthorizationByPathAndMethod(path,method);
//
//            List<RoleAuthorization> roles = authorization.getRoleAuthorizations();
//            RoleAuthorization roleAuthorization = RoleAuthorization.builder()
//                    .authorization(authorization)
//                    .role(role)
//                    .build();
//
//            roles.add(roleAuthorization);
//            roleAuthorizationRepository.save(roleAuthorization);
//        } else {
//            authorization = Authorization.builder()
//                    .path(path)
//                    .method(method)
//                    .priority(createAuthorizationRequest.getPriority())
//                    .roleAuthorizations(new ArrayList<>())
//                    .build();
//
//            RoleAuthorization roleAuthorization = RoleAuthorization.builder()
//                    .role(role)
//                    .authorization(authorization).build();
//            authorization.getRoleAuthorizations().add(roleAuthorization);
//            authorizationRepository.save(authorization);
//            roleAuthorizationRepository.save(roleAuthorization);
//        }
//
//        return CreateAuthorizationResponse.toDTO(authorization);
//    }

//    public DeleteAuthorizationResponse deleteAuthorization(Long authorizationId) {
//        Authorization authorization = authorizationRepository.findById(authorizationId).orElseThrow();
//        authorizationRepository.delete(authorization);
//        return DeleteAuthorizationResponse.toDTO(authorization);
//    }
}
