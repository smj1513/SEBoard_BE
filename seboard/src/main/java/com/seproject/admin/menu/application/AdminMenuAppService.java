package com.seproject.admin.menu.application;

import com.seproject.admin.menu.service.MenuService;
import com.seproject.admin.menu.utils.DelegatingMenuProvider;
import com.seproject.board.menu.domain.Menu;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.seproject.admin.menu.controller.dto.MenuDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminMenuAppService {

    private final MenuService menuService;
    private final DelegatingMenuProvider menuProvider;

    @Transactional
    public void createMenu(CreateMenuRequest request, String categoryType) {
        String name = request.getName();
        if(name == null) {
            throw new CustomIllegalArgumentException(ErrorCode.MENU_NAME_NULL,null);
        }

        menuProvider.create(request,categoryType);
    }

    public List<MenuResponse> findAllMenuTree() {
        Map<Menu, List<Menu>> menuListMap = menuService.findAllMenuTree();

        List<MenuResponse> menuResponses = new ArrayList<>();
        menuListMap.forEach((menu, subMenus) -> {
            List<MenuResponse> subMenuResponses = subMenus.stream()
                    .map((v) -> new MenuResponse(v, null))
                    .collect(Collectors.toList());
            menuResponses.add(new MenuResponse(menu,subMenuResponses));
        });

        return menuResponses;
    }

    public MenuResponse findMenu(Long menuId) {
        Menu menu = menuService.findById(menuId);
        List<MenuResponse> subMenus = menuService.findSubMenu(menuId)
                .stream()
                .map((v) -> new MenuResponse(v, null))
                .collect(Collectors.toList());
        return new MenuResponse(menu,subMenus);
    }

    @Transactional
    public void delete(Long menuId) {
        menuService.delete(menuId);
    }


    @Transactional
    public void migrateCategory(Long fromMenuId, Long toMenuId){
        menuService.changeSuperCategory(fromMenuId,toMenuId);
    }

    @Transactional
    public void update(Long categoryId, UpdateMenuRequest request) {
        Menu menu = menuService.findById(categoryId);

        menuProvider.update(menu,request);
    }
}
