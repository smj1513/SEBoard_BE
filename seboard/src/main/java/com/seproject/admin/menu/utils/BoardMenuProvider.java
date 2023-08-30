package com.seproject.admin.menu.utils;


import com.seproject.account.authorization.domain.MenuAccessAuthorization;
import com.seproject.account.authorization.domain.MenuExposeAuthorization;
import com.seproject.account.authorization.service.AuthorizationService;
import com.seproject.account.role.domain.Role;
import com.seproject.account.role.service.RoleService;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.menu.controller.dto.MenuDTO;
import com.seproject.admin.menu.service.MenuService;
import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Menu;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;

import java.util.List;

public class BoardMenuProvider extends AbstractMenuProvider {

    public BoardMenuProvider(MenuService menuService, RoleService roleService, AuthorizationService authorizationService) {
        super(menuService, roleService, authorizationService);
    }

    @Override
    protected boolean support(Menu menu) {
        return BoardMenu.class.isAssignableFrom(menu.getClass());
    }

    @Override
    public Long create(MenuDTO.CreateMenuRequest request, String categoryType) {
        if (categoryType.equals("BOARD")) {

            Long superCategoryId = request.getSuperCategoryId();
            String name = request.getName();
            String description = request.getDescription();
            String urlInfo = request.getUrlId();

            if(superCategoryId == null) {
                throw new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_MENU,null);
            }

            Menu superMenu = menuService.findById(superCategoryId);
            Long boardMenuId = menuService.createBoardMenu(superMenu, name, description, urlInfo);

            MenuDTO.MenuAuthOption expose = request.getExpose();
            MenuDTO.MenuAuthOption access = request.getAccess();

            if(access == null || expose == null) {
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);
            }

            Menu menu = menuService.findById(boardMenuId);

            SelectOption accessSelectOption = SelectOption.of(access.getName());
            List<Role> accessRoles = roleService.convertRoles(accessSelectOption);
            authorizationService.updateAccess(menu,accessSelectOption,accessRoles);

            SelectOption exposeSelectOption = SelectOption.of(expose.getName());
            List<Role> exposeRoles = roleService.convertRoles(exposeSelectOption);
            authorizationService.updateExpose(menu,exposeSelectOption,exposeRoles);

            return boardMenuId;
        }

        return null;
    }

    @Override
    public Long update(Menu menu, MenuDTO.UpdateMenuRequest request) {
        if (support(menu)) {
            String description = request.getDescription();
            String name = request.getName();
            String urlId = request.getUrlId();

            menu.changeDescription(description);
            menu.changeName(name);
            menu.changeUrlInfo(urlId);

            MenuDTO.MenuAuthOption access = request.getAccess();
            MenuDTO.MenuAuthOption expose = request.getExpose();

            if (access == null || expose == null)
                throw new CustomIllegalArgumentException(ErrorCode.INVALID_MENU_REQUEST, null);

            List<Role> accessRoles = parseRoles(access);
            SelectOption accessSelectOption = SelectOption.of(access.getName());
            MenuAccessAuthorization menuAccessAuthorization = new MenuAccessAuthorization(menu);
            menuAccessAuthorization.update(accessRoles);
            menuAccessAuthorization.setSelectOption(accessSelectOption);

            List<Role> exposeRoles = parseRoles(expose);
            SelectOption exposeSelectOption = SelectOption.of(expose.getName());
            MenuExposeAuthorization menuExposeAuthorization = new MenuExposeAuthorization(menu);
            menuExposeAuthorization.update(exposeRoles);
            menuExposeAuthorization.setSelectOption(exposeSelectOption);

            menu.updateMenuAuthorizations(List.of(menuAccessAuthorization,menuExposeAuthorization));
            return menu.getMenuId();
        }

        return null;
    }

    @Override
    public MenuDTO.MenuResponse toDto(Menu menu) {
        if (support(menu)) {

        }
        return null;
    }
}