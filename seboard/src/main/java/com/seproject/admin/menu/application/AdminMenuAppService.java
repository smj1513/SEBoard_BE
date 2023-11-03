package com.seproject.admin.menu.application;

import com.seproject.admin.menu.service.AdminMenuService;
import com.seproject.admin.menu.utils.DelegatingMenuProvider;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.menu.service.MenuService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.seproject.admin.menu.controller.dto.MenuDTO.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminMenuAppService {

    private final AdminMenuService adminMenuService;
    private final MenuService menuService;
    private final DelegatingMenuProvider menuProvider;

    @Transactional
    public void createMenu(CreateMenuRequest request, String categoryType) {
        String name = request.getName();
        if (name == null) {
            throw new CustomIllegalArgumentException(ErrorCode.MENU_NAME_NULL, null);
        }

        menuProvider.create(request, categoryType);
    }

    public List<MenuResponse> findAllMenuTree() {
        //TODO : 성능 개선?
        List<Menu> depth0Menus = menuService.findByDepth(0);
        List<MenuResponse> menuResponses = depth0Menus.stream()
                .map(
                        (v) -> new MenuResponse(
                                v, null,
                                v.getAvailableRoles().entrySet().stream()
                                        .collect(
                                                Collectors.toMap(
                                                        (entry) -> entry.getKey(),
                                                        (entry) -> Pair.of(
                                                                entry.getValue().getFirst().getName(),
                                                                entry.getValue().getSecond().stream()
                                                                        .map(role -> role.getName())
                                                                        .collect(Collectors.toList())
                                                        )
                                                )
                                        )
                        )
                )
                .collect(Collectors.toList());

        menuResponses.forEach(this::findAllMenuTree);

        return menuResponses;

        //depth 1까지 밖에 조회 불가능
//        Map<Menu, List<Menu>> menuListMap = menuService.findAllMenuTree();
//
//        List<MenuResponse> menuResponses = new ArrayList<>();
//        menuListMap.forEach((menu, subMenus) -> {
//            List<MenuResponse> subMenuResponses = subMenus.stream()
//                    .map((v) -> new MenuResponse(v, null))
//                    .collect(Collectors.toList());
//            menuResponses.add(new MenuResponse(menu,subMenuResponses));
//        });
    }

    private void findAllMenuTree(MenuResponse superMenus) {
        List<MenuResponse> subMenus = menuService.findSubMenu(superMenus.getMenuId()).stream()
                .map(
                        (v) -> new MenuResponse(
                                v, null,
                                v.getAvailableRoles().entrySet().stream()
                                        .collect(
                                                Collectors.toMap(
                                                        (entry) -> entry.getKey(),
                                                        (entry) -> Pair.of(
                                                                entry.getValue().getFirst().getName(),
                                                                entry.getValue().getSecond().stream()
                                                                        .map(role -> role.getName())
                                                                        .collect(Collectors.toList())
                                                        )
                                                )
                                        )
                        )
                )
                .collect(Collectors.toList());

        superMenus.setSubMenus(subMenus);

        subMenus.forEach(this::findAllMenuTree);
    }

    public MenuResponse findMenu(Long menuId) {
        Menu menu = menuService.findById(menuId);
        List<MenuResponse> subMenus = menuService.findSubMenu(menuId)
                .stream()
                .map((v) -> new MenuResponse(v, null))
                .collect(Collectors.toList());
        return new MenuResponse(menu, subMenus);
    }

    @Transactional
    public void delete(Long menuId) {
        adminMenuService.delete(menuId);
    }


    @Transactional
    public void migrateCategory(Long fromMenuId, Long toMenuId) {
        adminMenuService.changeSuperCategory(fromMenuId, toMenuId);
    }

    @Transactional
    public void update(Long categoryId, UpdateMenuRequest request) {
        Menu menu = menuService.findById(categoryId);

        menuProvider.update(menu, request);
    }
}
