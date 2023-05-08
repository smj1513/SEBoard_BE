package com.seproject.account.service;

import com.seproject.account.model.role.Authorization;
import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.repository.role.AuthorizationRepository;
import com.seproject.account.repository.role.RoleAuthorizationRepository;
import com.seproject.account.repository.role.RoleRepository;
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

import static com.seproject.admin.dto.AuthorizationDTO.*;

@RequiredArgsConstructor
@Service
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;
    private final RoleAuthorizationRepository roleAuthorizationRepository;
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

    public RetrieveAllAuthorizationResponse findAllAuthorization() {
        List<Authorization> allAuthorization = authorizationRepository.findAllAuthorization();
        return RetrieveAllAuthorizationResponse.toDTO(allAuthorization);
    }

    @Transactional
    public CreateAuthorizationResponse addAuthorization(CreateAuthorizationRequest createAuthorizationRequest) {
        String path = createAuthorizationRequest.getPath();
        String method = createAuthorizationRequest.getMethod();
        Role role = roleRepository.findByName(createAuthorizationRequest.getRole()).orElseThrow();
        Authorization authorization;
        if(authorizationRepository.existsByPathAndMethod(path, method)) {
            authorization = authorizationRepository.findAuthorizationByPathAndMethod(path,method);

            List<RoleAuthorization> roles = authorization.getRoleAuthorizations();
            RoleAuthorization roleAuthorization = RoleAuthorization.builder()
                    .authorization(authorization)
                    .role(role)
                    .build();

            roles.add(roleAuthorization);
            roleAuthorizationRepository.save(roleAuthorization);
        } else {
            authorization = Authorization.builder()
                    .path(path)
                    .method(method)
                    .priority(createAuthorizationRequest.getPriority())
                    .roleAuthorizations(new ArrayList<>())
                    .build();

            RoleAuthorization roleAuthorization = RoleAuthorization.builder()
                    .role(role)
                    .authorization(authorization).build();
            authorization.getRoleAuthorizations().add(roleAuthorization);
            authorizationRepository.save(authorization);
            roleAuthorizationRepository.save(roleAuthorization);
        }

        return CreateAuthorizationResponse.toDTO(authorization);
    }

    public DeleteAuthorizationResponse deleteAuthorization(Long authorizationId) {
        Authorization authorization = authorizationRepository.findById(authorizationId).orElseThrow();
        authorizationRepository.delete(authorization);
        return DeleteAuthorizationResponse.toDTO(authorization);
    }
}
