package com.seproject.admin.service;

import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.account.repository.role.RoleAuthorizationRepository;
import com.seproject.account.repository.role.auth.AuthorizationRepository;
import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.domain.repository.MenuExposeRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.seboard.domain.model.category.BoardMenu;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.ExternalSiteMenu;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.seproject.admin.dto.AuthorizationDTO.*;


@RequiredArgsConstructor
@Transactional
@Service
public class AdminMenuService {

    private final MenuRepository menuRepository;
    private final RoleService roleService;
    private final AuthorizationRepository authorizationRepository;
    private final MenuExposeRepository menuExposeRepository;

    public CategoryAccessOptionResponse retrieve(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND, null));
        List<MenuAuthorization> menuAuthorizations = menu.getMenuAuthorizations();
        List<MenuAuthorization> write = new ArrayList<>();
        List<MenuAuthorization> expose = new ArrayList<>();
        List<MenuAuthorization> manage = new ArrayList<>();

        for (MenuAuthorization menuAuthorization : menuAuthorizations) {
            AccessOption accessOption = menuAuthorization.getAccessOption();
            if(accessOption == AccessOption.WRITE) {
                write.add(menuAuthorization);
            } else if(accessOption == AccessOption.EXPOSE) {
                expose.add(menuAuthorization);
            } else if(accessOption == AccessOption.MANAGE) {
                manage.add(menuAuthorization);
            } else {
                throw new RuntimeException("있을수 없는 일...");
            }
        }

        Optional<Authorization> authorization = authorizationRepository.findByPath("/category/" + menuId + "/**");

        MenuAuthorizationResponse writeResponse = MenuAuthorizationResponse.toDTO(write);
        MenuAuthorizationResponse exposeResponse = MenuAuthorizationResponse.toDTO(expose);
        MenuAuthorizationResponse manageResponse = MenuAuthorizationResponse.toDTO(manage);

        if(authorization.isPresent()) {
            Authorization auth = authorization.get();
            List<RoleAuthorization> roleAuthorizations = auth.getRoleAuthorizations();

            List<Role> roles = roleAuthorizations.stream().map(RoleAuthorization::getRole).collect(Collectors.toList());
            SelectOption selectOption = auth.getSelectOption();
            AccessResponse accessResponse = AccessResponse.toDTO(roles,selectOption);
            return CategoryAccessOptionResponse.toDTO(menu,accessResponse,writeResponse,manageResponse,exposeResponse);
        } else {
            return CategoryAccessOptionResponse.toDTO(menu,null,writeResponse,manageResponse,exposeResponse);
        }

    }

    public void update(Long categoryId, CategoryAccessUpdateRequest request) {
        Menu menu = menuRepository.findById(categoryId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.ROLE_NOT_FOUND, null));

        Class<? extends Menu> menuClass = menu.getClass();
        menuExposeRepository.deleteAllInBatch(menu.getMenuAuthorizations());
        if (menuClass == Menu.class) {
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if (menuExpose == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);

            update(menu,menuExpose,AccessOption.EXPOSE);
        } else if (menuClass == BoardMenu.class) {
            CategoryAccessUpdateRequestElement access = request.getAccess();
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if (access == null || menuExpose == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);

            update(menu,menuExpose,AccessOption.EXPOSE);
            accessUpdate(menu,access);

        } else if (menuClass == ExternalSiteMenu.class) {
            CategoryAccessUpdateRequestElement menuExpose = request.getMenuExpose();

            if (menuExpose == null) throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);

            update(menu,menuExpose,AccessOption.EXPOSE);
        } else if (menuClass == Category.class) {
            CategoryAccessUpdateRequestElement write = request.getWrite();
            CategoryAccessUpdateRequestElement manager = request.getManage();

            if (write == null || manager == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);

            update(menu,write,AccessOption.WRITE);
            update(menu,manager,AccessOption.MANAGE);
        } else {
            throw new CustomIllegalArgumentException(ErrorCode.ILLEGAL_MENU_TYPE, null);
        }
    }

    private void update(Menu menu ,CategoryAccessUpdateRequestElement element, AccessOption accessOption) {
        SelectOption selectOption = SelectOption.of(element.getOption());
        update(menu,selectOption,accessOption);
    }

    private void accessUpdate(Menu menu,CategoryAccessUpdateRequestElement access) {
        SelectOption selectOption = SelectOption.of(access.getOption());
        accessUpdate(menu,selectOption);
    }

    public void update(Menu menu , SelectOption selectOption, AccessOption accessOption) {
        List<Role> roles = roleService.convertRoles(selectOption);

        List<MenuAuthorization> collect = roles.stream().map(role ->
                MenuAuthorization.builder()
                        .menu(menu)
                        .role(role)
                        .accessOption(accessOption)
                        .selectOption(selectOption)
                        .build()).collect(Collectors.toList());

        menu.updateMenuAuthorizations(collect);
    }

    public void accessUpdate(Menu menu,SelectOption selectOption) {
        List<Role> roles = roleService.convertRoles(selectOption);
        String path = "/category/" + menu.getMenuId() + "/**";

        Optional<Authorization> optional = authorizationRepository.findByPath(path);

        if(optional.isPresent()) {
            Authorization authorization = optional.get();

            List<RoleAuthorization> collect = roles.stream().map(role ->
                    RoleAuthorization.builder()
                            .role(role)
                            .authorization(authorization)
                            .build()).collect(Collectors.toList());

            authorization.setRoleAuthorizations(collect);
            authorization.setSelectOption(selectOption);

            authorizationRepository.save(authorization);
        } else {

            Authorization authorization = Authorization.builder()
                    .path(path)
                    .build();

            List<RoleAuthorization> collect = roles.stream().map(role ->
                    RoleAuthorization.builder()
                            .role(role)
                            .authorization(authorization)
                            .build()).collect(Collectors.toList());

            authorization.setRoleAuthorizations(collect);
            authorization.setSelectOption(selectOption);

            authorizationRepository.save(authorization);
        }


    }

}
