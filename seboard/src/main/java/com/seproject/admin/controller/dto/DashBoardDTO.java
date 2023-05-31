package com.seproject.admin.controller.dto;

import com.seproject.account.model.role.auth.Authorization;
import com.seproject.admin.domain.SelectOption;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
            String option = getOrAll(menuEdit);
            return builder()
                    .menuEdit(DashBoardRetrieveElement.toDTO("SE 메뉴 편집",option))
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
            String accountManageSelectOption = getOrAll(accountManage);
            String accountPolicySelectOption = getOrAll(accountPolicy);
            String roleManageSelectOption = getOrAll(roleManage);

            return builder()
                    .accountList(DashBoardRetrieveElement.toDTO("회원 목록",accountManageSelectOption))
                    .accountPolicy(DashBoardRetrieveElement.toDTO("회원 정책",accountPolicySelectOption))
                    .roles(DashBoardRetrieveElement.toDTO("회원 그룹",roleManageSelectOption))
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
            String postSelectOption = getOrAll(postAuth);
            String commentSelectOption = getOrAll(commentAuth);
            String fileSelectOption = getOrAll(fileAuth);
            String trashSelectOption = getOrAll(trashAuth);

            return builder()
                    .post(DashBoardRetrieveElement.toDTO("게시글 관리",postSelectOption))
                    .comment(DashBoardRetrieveElement.toDTO("댓글 관리",commentSelectOption))
                    .file(DashBoardRetrieveElement.toDTO("첨부파일 관리",fileSelectOption))
                    .trash(DashBoardRetrieveElement.toDTO("휴지통",trashSelectOption))
                    .build();
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class GeneralSettingResponse {
        private DashBoardRetrieveElement general;
        private DashBoardRetrieveElement mainPage;

        public static GeneralSettingResponse toDTO(Authorization generalAuth,Authorization mainPageAuth) {

            String generalSelectOption = getOrAll(generalAuth);
            String mainPageSelectOption = getOrAll(mainPageAuth);

            return builder()
                    .general(DashBoardRetrieveElement.toDTO("일반",generalSelectOption))
                    .mainPage(DashBoardRetrieveElement.toDTO("메인 페이지 설정",mainPageSelectOption))
                    .build();
        }
    }


    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class DashBoardRetrieveElement {
        private String name;
        private String option;

        public static DashBoardRetrieveElement toDTO(String name, String option) {
            return builder()
                    .name(name)
                    .option(option)
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
