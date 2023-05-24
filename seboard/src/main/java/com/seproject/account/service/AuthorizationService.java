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
import com.seproject.admin.controller.MenuAccessOption;
import com.seproject.admin.dto.AuthorizationDTO;
import com.seproject.admin.service.MenuExposeService;
import com.seproject.admin.service.RoleService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.seboard.domain.model.category.BoardMenu;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.ExternalSiteMenu;
import com.seproject.seboard.domain.model.category.Menu;
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

import static com.seproject.admin.dto.AuthorizationDTO.*;

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

    private final MenuExposeService menuExposeService;
    private final RoleService roleService;

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
    public void update(Long categoryId, CategoryAccessUpdateRequest request) {
        Menu menu = menuRepository.findById(categoryId).orElseThrow(()->new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND,null));
        Class<? extends Menu> menuClass = menu.getClass();
        if(menuClass == Menu.class) {
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if(menuExpose == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST,null);

            updateMenuExpose(menu,menuExpose);
        } else if(menuClass == BoardMenu.class) {
            CategoryAccessUpdateRequestElement access = request.getAccess();
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if(access == null || menuExpose == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST,null);

            updateAccess(categoryId,access.getRoles());
            updateMenuExpose(menu,menuExpose);
        } else if(menuClass == ExternalSiteMenu.class) {
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if(menuExpose == null) throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST,null);

            updateMenuExpose(menu,menuExpose);
        } else if(menuClass == Category.class){
            CategoryAccessUpdateRequestElement write = request.getWrite();
            CategoryAccessUpdateRequestElement manager = request.getManage();

            if(write == null || manager == null) throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST,null);

            updateWrite(categoryId,write.getRoles());
            updateManager(categoryId,request.getManage().getRoles());
        } else {
            throw new CustomIllegalArgumentException(ErrorCode.ILLEGAL_MENU_TYPE,null);
        }
    }

    private void updateMenuExpose(Menu menu,CategoryAccessUpdateRequestElement menuExpose) {
        //TODO : 어딘가에 저장
        String option = menuExpose.getOption();
        MenuAccessOption menuAccessOption = MenuAccessOption.of(option);

        List<Role> roles = roleService.convertRoles(menuAccessOption);
        menuExposeService.updateMenuExpose(menu,roles);
    }

    private void updateAccess(Long categoryId, List<Long> roleIds) {
        List<CategoryAuthorization> authorizations = categoryAuthorizationRepository.findByCategoryId(categoryId);
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

    private void updateWrite(Long categoryId, List<Long> roleIds) {
        CategoryAuthorization authorization = categoryAuthorizationRepository.findByCategoryIdAndMethod(categoryId, "POST");

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

    private void updateManager(Long categoryId, List<Long> roleIds) {
        List<CategoryManagerAuthorization> authorizations = categoryManagerAuthorizationRepository.findByCategoryId(categoryId);

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

}
