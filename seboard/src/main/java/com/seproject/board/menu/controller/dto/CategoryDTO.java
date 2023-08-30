package com.seproject.board.menu.controller.dto;

import com.seproject.account.authorization.domain.MenuAuthorization;
import com.seproject.board.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CategoryResponseElement {
        private String name;
        private Long menuId;
        private String urlId;

        //TODO : Access, Manage, Edit, Expose 추가

        public static CategoryResponseElement toDTO(Menu menu) {
            return builder()
                    .name(menu.getName())
                    .menuId(menu.getMenuId())
                    .urlId(menu.getUrlInfo())
                    .build();
        }
    }


}
