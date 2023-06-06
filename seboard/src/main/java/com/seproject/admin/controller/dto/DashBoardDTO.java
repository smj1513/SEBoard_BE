package com.seproject.admin.controller.dto;

import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.admin.domain.SelectOption;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class DashBoardDTO {


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardRetrieveResponse {
       private MenuSettingResponse menuSetting;
       private AccountManageResponse accountManage;
       private ContentManageResponse contentManage;
       private GeneralSettingResponse generalSetting;

       public static DashBoardRetrieveResponse toDTO(MenuSettingResponse menuSetting,
                                                     AccountManageResponse accountManage,
                                                     ContentManageResponse contentManage,
                                                     GeneralSettingResponse generalSetting) {
           return builder()
                   .menuSetting(menuSetting)
                   .accountManage(accountManage)
                   .contentManage(contentManage)
                   .generalSetting(generalSetting)
                   .build();
       }
    }

    private static String getOrAll(Authorization authorization) {

        return authorization == null ? SelectOption.ALL.getName() : authorization.getSelectOption().getName();
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class MenuSettingResponse {
        private DashBoardRetrieveElement menuEdit;

        public static MenuSettingResponse toDTO(Authorization menuEdit) {
            return builder()
                    .menuEdit(DashBoardRetrieveElement.toDTO("SE 메뉴 편집",menuEdit))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class AccountManageResponse {
        private DashBoardRetrieveElement accountList;
        private DashBoardRetrieveElement accountPolicy;
        private DashBoardRetrieveElement roles;

        public static AccountManageResponse toDTO(Authorization accountManage, Authorization accountPolicy, Authorization roleManage) {

            return builder()
                    .accountList(DashBoardRetrieveElement.toDTO("회원 목록",accountManage))
                    .accountPolicy(DashBoardRetrieveElement.toDTO("회원 정책",accountPolicy))
                    .roles(DashBoardRetrieveElement.toDTO("회원 그룹",roleManage))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ContentManageResponse {
        private DashBoardRetrieveElement post;
        private DashBoardRetrieveElement comment;
        private DashBoardRetrieveElement file;
        private DashBoardRetrieveElement trash;

        public static ContentManageResponse toDTO(Authorization postAuth,
                                                  Authorization commentAuth,
                                                  Authorization fileAuth,
                                                  Authorization trashAuth
                                                  ) {
            return builder()
                    .post(DashBoardRetrieveElement.toDTO("게시글 관리",postAuth))
                    .comment(DashBoardRetrieveElement.toDTO("댓글 관리",commentAuth))
                    .file(DashBoardRetrieveElement.toDTO("첨부파일 관리",fileAuth))
                    .trash(DashBoardRetrieveElement.toDTO("휴지통",trashAuth))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class GeneralSettingResponse {
        private DashBoardRetrieveElement general;
        private DashBoardRetrieveElement mainPage;

        public static GeneralSettingResponse toDTO(Authorization generalAuth,Authorization mainPageAuth) {
            return builder()
                    .general(DashBoardRetrieveElement.toDTO("일반",generalAuth))
                    .mainPage(DashBoardRetrieveElement.toDTO("메인 페이지 설정",mainPageAuth))
                    .build();
        }
    }


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardRetrieveElement {
        private String name;
        private String option;
        private List<String> roles;

        public static DashBoardRetrieveElement toDTO(String name, Authorization authorization) {
            String option = getOrAll(authorization);
            List<String> collect = authorization.getRoleAuthorizations()
                    .stream()
                    .map(RoleAuthorization::getRole)
                    .map(Role::toString)
                    .collect(Collectors.toList());
            return builder()
                    .name(name)
                    .option(option)
                    .roles(collect)
                    .build();
        }
    }

    @Data
    public static class DashBoardUpdateRequest {
        private MenuSettingRequest menuSetting;
        private AccountManageRequest accountManage;
        private ContentManageRequest contentManage;
        private GeneralSettingRequest generalSetting;
    }

    @Data
    public static class MenuSettingRequest {
        private DashBoardRetrieveElement menuEdit;
    }

    @Data
    public static class AccountManageRequest {
        private DashBoardRetrieveElement accountList;
        private DashBoardRetrieveElement accountPolicy;
        private DashBoardRetrieveElement roles;
    }

    @Data
    public static class ContentManageRequest {
        private DashBoardRetrieveElement post;
        private DashBoardRetrieveElement comment;
        private DashBoardRetrieveElement file;
        private DashBoardRetrieveElement trash;
    }

    @Data
    public static class GeneralSettingRequest {
        private DashBoardRetrieveElement general;
        private DashBoardRetrieveElement mainPage;
    }

    @Data
    public static class DashBoardUpdateElement {
        private String option;
    }

}
