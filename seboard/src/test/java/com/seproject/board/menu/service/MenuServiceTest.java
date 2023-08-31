package com.seproject.board.menu.service;

import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.Menu;
import com.seproject.global.MenuSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired MenuService menuService;
    @Autowired MenuSetup menuSetup;
    @Autowired EntityManager em;


    @Test
    void findSubMenu_하위메뉴_매핑_테스트() {

        List<Menu> rootMenus = new ArrayList<>();

        Map<Long,Integer> map = new HashMap<>();

        List<Integer> boardMenusSize = List.of(3,2,4);

        List<List<Integer>> categorySize = List.of(
                List.of(2,2,4),
                List.of(4,3),
                List.of(6,5,4,3)
        );

        for (int i = 0; i < 3; i++) {
            Menu root = menuSetup.createMenu();
            rootMenus.add(root);

            for (int j = 0; j < boardMenusSize.get(i); j++) {
                BoardMenu boardMenu = menuSetup.createBoardMenu(root);

                for (int k = 0; k < categorySize.get(i).get(j); k++) {
                    Category category = menuSetup.createCategory(boardMenu);
                }

                em.flush(); em.clear();

                Long menuId = boardMenu.getMenuId();
                map.put(menuId,categorySize.get(i).get(j));
            }

            map.put(root.getMenuId(),boardMenusSize.get(i));
        }

        List<Long> rootIds = rootMenus.stream()
                .map(Menu::getMenuId)
                .collect(Collectors.toList());

        Map<Long, List<Menu>> subMenu = menuService.findSubMenu(rootIds);

        subMenu.forEach((k,v) -> {
            Integer size = map.get(k);
            assertEquals(size,v.size());
        });


    }
}