package com.seproject.admin.dashboard.controller.dto;

import com.seproject.account.role.domain.Role;
import com.seproject.account.role.domain.RoleAuthorization;
import com.seproject.account.authorization.domain.Authorization;
import com.seproject.admin.dashboard.domain.DashBoardMenu;
import com.seproject.admin.dashboard.domain.DashBoardMenuGroup;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.menu.controller.dto.MenuDTO;
import com.seproject.admin.role.controller.dto.RoleDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashBoardDTO {

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardMenuResponseElement {

        private Long id;
        private String name;
        private String desc;
        private String url;

        public static DashBoardMenuResponseElement toDTO(DashBoardMenu dashBoardMenu) {
            return builder()
                    .id(dashBoardMenu.getId())
                    .name(dashBoardMenu.getName())
                    .url(dashBoardMenu.getUrl())
                    .build();
        }
    }
    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardMenuResponse {

        private List<DashBoardMenuResponseElement> menu;
        private List<DashBoardMenuResponseElement> person;
        private List<DashBoardMenuResponseElement> content;
        private List<DashBoardMenuResponseElement> setting;

        public static DashBoardMenuResponse toDTO(List<DashBoardMenu> dashBoardMenus) {
            List<DashBoardMenuResponseElement> menu = new ArrayList<>();
            List<DashBoardMenuResponseElement> person = new ArrayList<>();
            List<DashBoardMenuResponseElement> content = new ArrayList<>();
            List<DashBoardMenuResponseElement> setting = new ArrayList<>();

            for (DashBoardMenu dashBoardMenu : dashBoardMenus) {
                List<DashBoardMenuResponseElement> target;

                if(dashBoardMenu.getGroup().equals(DashBoardMenuGroup.MENU_GROUP)) {
                    target = menu;
                } else if(dashBoardMenu.getGroup().equals(DashBoardMenuGroup.PERSON_GROUP)) {
                    target = person;
                } else if(dashBoardMenu.getGroup().equals(DashBoardMenuGroup.CONTENT_GROUP)) {
                    target = content;
                } else {
                    target = setting;
                }

                target.add(DashBoardMenuResponseElement.toDTO(dashBoardMenu));
            }

            return builder()
                    .menu(menu)
                    .person(person)
                    .content(content)
                    .setting(setting)
                    .build();
        }



    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardAuthorizationResponse {
        private DashBoardMenuResponse menu;
        private List<RoleDTO.RoleResponse> roles;



    }


    @Data
    public static class DashBoardUpdateRequest {
        private Long id;
        private MenuDTO.MenuAuthOption option;

    }

}
