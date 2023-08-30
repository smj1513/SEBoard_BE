package com.seproject.board.menu.controller.dto;


import com.seproject.board.menu.domain.BoardMenu;
import com.seproject.board.menu.domain.Category;
import com.seproject.board.menu.domain.ExternalSiteMenu;
import com.seproject.board.menu.domain.Menu;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private Long menuId;
    private String name;
    private String urlId;
    private String externalUrl;
    private String type;
    private List<CategoryResponse> subMenu = new ArrayList<>();

    public CategoryResponse(Menu menu) {
        this.menuId = menu.getMenuId();
        this.name = menu.getName();

        if(menu.getClass()== ExternalSiteMenu.class){
            this.externalUrl = menu.getUrlInfo();
        }else{
            this.urlId = menu.getUrlInfo();
        }

        if(menu.getClass()==Menu.class){
            this.type = "MENU";
        }else if(menu.getClass()== BoardMenu.class){
            this.type = "BOARD";
        }else if(menu.getClass()== Category.class){
            this.type = "CATEGORY";
        }else if(menu.getClass()== ExternalSiteMenu.class) {
            this.type = "EXTERNAL";
        }

    }

    public void addSubMenu(CategoryResponse subMenu){
        this.subMenu.add(subMenu);
    }
}
