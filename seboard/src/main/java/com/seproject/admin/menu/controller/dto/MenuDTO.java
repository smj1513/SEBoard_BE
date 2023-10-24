package com.seproject.admin.menu.controller.dto;

import com.seproject.account.authorization.domain.MenuAuthorization;
import com.seproject.board.menu.controller.dto.CategoryDTO;
import com.seproject.board.menu.domain.Menu;
import lombok.Data;

import java.util.List;

public class MenuDTO {

    @Data
    public static class MenuResponse {
        private Long menuId;
        private String name;
        private String urlId;
        private MenuAuthOption access;
        private MenuAuthOption edit;
        private MenuAuthOption manage;
        private MenuAuthOption expose;
        private List<MenuResponse> subMenus;

        public MenuResponse(Menu menu, List<MenuResponse> subMenus) {
            this.menuId = menu.getMenuId();
            this.name = menu.getName();
            this.urlId = menu.getUrlInfo();
            this.subMenus = subMenus;
        }
    }

    public static class MenuAuthResponse {
        private String select;
        private List<String> roles;

        public MenuAuthResponse(String select, List<String> roles) {
            this.select = select;
            this.roles = roles;
        }

    }

    @Data
    public static class CreateMenuRequest {

        private Long superCategoryId;
        private String name;
        private String description;
        private String urlId;
        private String externalUrl;

        private MenuAuthOption access;
        private MenuAuthOption edit;
        private MenuAuthOption expose;
        private MenuAuthOption manage;
    }

    @Data
    public static class UpdateMenuRequest {
        private String name;
        private String description;
        private String urlId;
        private String externalUrl;

        private MenuAuthOption access;
        private MenuAuthOption edit;
        private MenuAuthOption expose;
        private MenuAuthOption manage;
    }

    @Data
    public static class MenuAuthOption {
        private String option;
        private List<Long> roles;
    }


}
