package com.seproject.admin.bulletin.application;

import com.seproject.admin.bulletin.controller.dto.MainPageDTO.InternalSiteMenuResponse;
import com.seproject.admin.bulletin.controller.dto.MainPageDTO.MainPageMenuResponse;
import com.seproject.admin.menu.service.AdminMenuService;
import com.seproject.board.bulletin.domain.model.MainPageMenu;
import com.seproject.board.bulletin.service.MainPageService;
import com.seproject.board.menu.domain.InternalSiteMenu;
import com.seproject.board.menu.domain.Menu;
import com.seproject.board.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AdminMainPageAppService {

    private final MainPageService mainPageService;
    private final MenuService menuService;

    @Transactional
    public Long createMainPageMenu(Long menuId) {
        Menu menu = menuService.findById(menuId);
        Long mainPageMenuId = mainPageService.createMainPageMenu(menu);
        return mainPageMenuId;
    }

    @Transactional
    public void deleteMainPageMenu(Long id) {
        mainPageService.deleteMainPageMenu(id);
    }

    @Transactional
    public void updateMainPageMenu(List<Long> menuIds) {
        List<MainPageMenu> allMenus = mainPageService.findAll();
        mainPageService.deleteMainPageMenus(allMenus);

        List<Menu> menus = menuService.findByIds(menuIds);
        mainPageService.createMainPageMenu(menus);
    }

    public List<MainPageMenuResponse> retrieveAllMainPageMenus() {
        List<MainPageMenu> all = mainPageService.findAll();
        return all.stream().map(MainPageMenuResponse::toDTO)
                .collect(Collectors.toList());
    }

    public List<InternalSiteMenuResponse> retrieveAllInternalSiteMenu() {
        List<InternalSiteMenu> mainPageableMenu = mainPageService.findAllMainPageableMenu();
        return mainPageableMenu.stream()
                .map(InternalSiteMenuResponse::toDTO)
                .collect(Collectors.toList());
    }
}
