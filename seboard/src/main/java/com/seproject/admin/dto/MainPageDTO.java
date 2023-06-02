package com.seproject.admin.dto;

import com.seproject.admin.domain.MainPageMenu;
import com.seproject.seboard.domain.model.category.InternalSiteMenu;
import com.seproject.seboard.domain.model.category.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class MainPageDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveAllMainPageMenuRequest {

        private List<RetrieveMainPageMenuRequest> mainPageMenus;

        public static RetrieveAllMainPageMenuRequest toDTO(List<MainPageMenu> menus) {

            return builder()
                    .mainPageMenus(menus.stream()
                            .map(RetrieveMainPageMenuRequest::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class AllInternalSiteMenuResponse {

        private List<InternalSiteMenuResponse> menus;

        public static AllInternalSiteMenuResponse toDTO(List<InternalSiteMenu> menus) {

            return builder()
                    .menus(menus.stream()
                            .map(InternalSiteMenuResponse::toDTO)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class InternalSiteMenuResponse {

        private Long categoryId;
        private String name;
        private String description;
        private String url;

        public static InternalSiteMenuResponse toDTO(InternalSiteMenu internalSiteMenu) {

            return builder()
                    .categoryId(internalSiteMenu.getMenuId())
                    .name(internalSiteMenu.getName())
                    .description(internalSiteMenu.getDescription())
                    .url(internalSiteMenu.getUrlInfo())
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class RetrieveMainPageMenuRequest {

        private Long id;
        private Long menuId;

        public static RetrieveMainPageMenuRequest toDTO(MainPageMenu mainPageMenu) {

            return builder()
                    .id(mainPageMenu.getId())
                    .menuId(mainPageMenu.getMenu().getMenuId())
                    .build();
        }
    }

    @Data
    public static class CreateMainPageMenuRequest {
        private Long menuId;
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class CreateMainPageMenuResponse {
        private Long id;
        private Long menuId;
        public static CreateMainPageMenuResponse toDTO(MainPageMenu mainPageMenu){
            return builder()
                    .id(mainPageMenu.getId())
                    .menuId(mainPageMenu.getMenu().getMenuId())
                    .build();
        }
    }

    @Data
    public static class DeleteMainPageMenuRequest {
        private Long id;
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DeleteMainPageMenuResponse {
        private Long id;
        private Long menuId;
        public static DeleteMainPageMenuResponse toDTO(MainPageMenu mainPageMenu){
            return builder()
                    .id(mainPageMenu.getId())
                    .menuId(mainPageMenu.getMenu().getMenuId())
                    .build();
        }
    }
}
