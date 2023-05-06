package com.seproject.seboard.controller.dto.post;


import com.seproject.seboard.domain.model.category.BoardMenu;
import com.seproject.seboard.domain.model.category.Category;
import com.seproject.seboard.domain.model.category.ExternalSiteMenu;
import com.seproject.seboard.domain.model.category.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

        if(menu.getClass()== ExternalSiteMenu.class){ //TODO : 이게 맞나?
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
