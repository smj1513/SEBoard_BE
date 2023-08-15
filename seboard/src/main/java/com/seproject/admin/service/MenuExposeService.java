//package com.seproject.admin.service;
//
//import com.seproject.account.role.domain.Role;
//import com.seproject.admin.domain.SelectOption;
//import com.seproject.account.authorization.domain.MenuAuthorization;
//import com.seproject.admin.domain.repository.MenuExposeRepository;
//import com.seproject.account.role.service.RoleService;
//import com.seproject.board.menu.domain.Menu;
//import com.seproject.board.menu.domain.repository.MenuRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class MenuExposeService {
//    private final MenuExposeRepository menuExposeRepository;
//    private final RoleService roleService;
//    private final MenuRepository menuRepository;
//
//    @Transactional
//    public List<MenuAuthorization> updateMenuExpose(Menu menu, SelectOption selectOption) {
//        List<Role> roles = roleService.convertRoles(selectOption);
//        List<MenuAuthorization> newMenuAuthorizations = roles.stream().map((role) -> MenuAuthorization.builder()
//                        .menu(menu)
//                        .role(role)
//                        .selectOption(selectOption)
//                        .build())
//                .collect(Collectors.toList());
//
//        List<MenuAuthorization> menuAuthorizations = menuExposeRepository.findAllByMenuId(menu.getMenuId());
//
//        menuExposeRepository.deleteAllInBatch(menuAuthorizations);
//        menuExposeRepository.saveAll(newMenuAuthorizations);
//
//        return newMenuAuthorizations;
//    }
//
//    public Map<Menu,List<Menu>> retrieveMenuByRole(List<Role> authorities) {
//        List<Long> roleIds = authorities.stream().map(Role::getId).collect(Collectors.toList());
//
//        List<Menu> menus = menuExposeRepository.findAllNotExposeSetting();
//        menus.addAll(menuExposeRepository.findAllByRoleId(roleIds));
//
//        List<Menu> findSubMenus = menuRepository.findAllBySuperMenuIds(menus.stream().map(Menu::getMenuId).collect(Collectors.toList()));
//        Map<Menu,List<Menu>> menuHierarchical = new HashMap<>();
//
//        for (Menu menu : menus) {
//            List<Menu> subMenus = new ArrayList<>();
//            for (Menu findSubMenu : findSubMenus) {
//                if(findSubMenu.getSuperMenu().equals(menu)) {
//                    subMenus.add(findSubMenu);
//                }
//            }
//            menuHierarchical.put(menu,subMenus);
//        }
//
//        return menuHierarchical;
//    }
//
//    public List<MenuAuthorization> retrieveMenuExposeByMenuId(Long menuId) {
//        return menuExposeRepository.findAllByMenuId(menuId);
//    }
//
//
//}
