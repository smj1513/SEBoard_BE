package com.seproject.admin.service;

import com.seproject.account.model.role.Role;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.Authorization;
import com.seproject.account.repository.role.RoleAuthorizationRepository;
import com.seproject.account.repository.role.auth.AuthorizationRepository;
import com.seproject.admin.controller.dto.DashBoardDTO;
import com.seproject.admin.domain.SelectOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.seproject.admin.controller.dto.DashBoardDTO.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDashBoardService {
    private static final String MENU_EDIT_URL = "/admin/menu";

    private static final String ACCOUNT_MANAGE_URL = "/admin/account";
    private static final String ACCOUNT_POLICY_URL = "/admin/accountPolicy";
    private static final String ROLE_MANAGE_URL = "/admin/roles";

    private static final String POST_MANAGE_URL = "/admin/posts";
    private static final String COMMENT_MANAGE_URL = "/admin/comments";
    private static final String FILE_MANAGE_URL = "/admin/files";
    private static final String TRASH_URL = "/admin/trash";

    private static final String GENERAL_URL = "/admin/general";
    private static final String MAIN_PAGE_MENU_MANAGE_URL = "/admin/mainPageMenu";


    private final AuthorizationRepository authorizationRepository;
    private final RoleAuthorizationRepository roleAuthorizationRepository;
    private final RoleService roleService;

    public DashBoardRetrieveResponse findDashBoardSettings() {

        Authorization menuEditAuth = authorizationRepository.findByPath(MENU_EDIT_URL).orElse(null);
        MenuSettingResponse menuSettingResponse = MenuSettingResponse.toDTO(menuEditAuth);

        Authorization accountManageAuth = authorizationRepository.findByPath(ACCOUNT_MANAGE_URL).orElse(null);
        Authorization accountPolicyAuth = authorizationRepository.findByPath(ACCOUNT_POLICY_URL).orElse(null);
        Authorization roleManageAuth = authorizationRepository.findByPath(ROLE_MANAGE_URL).orElse(null);
        AccountManageResponse accountManageResponse = AccountManageResponse.toDTO(accountManageAuth, accountPolicyAuth, roleManageAuth);

        Authorization postManageAuth = authorizationRepository.findByPath(POST_MANAGE_URL).orElse(null);
        Authorization commentManageAuth = authorizationRepository.findByPath(COMMENT_MANAGE_URL).orElse(null);
        Authorization fileManageAuth = authorizationRepository.findByPath(FILE_MANAGE_URL).orElse(null);
        Authorization trashManageAuth = authorizationRepository.findByPath(TRASH_URL).orElse(null);
        ContentManageResponse contentManageResponse = ContentManageResponse.toDTO(postManageAuth,commentManageAuth,fileManageAuth,trashManageAuth);

        Authorization generalAuth = authorizationRepository.findByPath(GENERAL_URL).orElse(null);
        Authorization mainPageAuth = authorizationRepository.findByPath(MAIN_PAGE_MENU_MANAGE_URL).orElse(null);
        GeneralSettingResponse generalSettingResponse = GeneralSettingResponse.toDTO(generalAuth, mainPageAuth);

        return DashBoardRetrieveResponse.toDTO(menuSettingResponse,accountManageResponse,contentManageResponse,generalSettingResponse);
    }

    private void update(String url, String option) {
        SelectOption selectOption = SelectOption.of(option);
        Optional<Authorization> optional = authorizationRepository.findByPath(url);
        if(optional.isPresent()) {
            Authorization authorization = optional.get();

            if(selectOption.equals(authorization.getSelectOption())) return;

            roleAuthorizationRepository.deleteAllInBatch(authorization.getRoleAuthorizations());

            List<RoleAuthorization> collect = roleService.convertRoles(selectOption)
                    .stream()
                    .map(role -> RoleAuthorization.builder()
                            .role(role)
                            .authorization(authorization)
                            .build())
                    .collect(Collectors.toList());

            authorization.setRoleAuthorizations(collect);
            authorization.setSelectOption(selectOption);
        } else {
            Authorization authorization = Authorization.builder()
                    .path(url)
                    .build();

            List<RoleAuthorization> collect = roleService.convertRoles(selectOption)
                    .stream()
                    .map(role -> RoleAuthorization.builder()
                            .role(role)
                            .authorization(authorization)
                            .build())
                    .collect(Collectors.toList());

            authorization.setRoleAuthorizations(collect);
            authorization.setSelectOption(selectOption);

            authorizationRepository.save(authorization);
        }

    }
    @Transactional
    public void updateDashBoardSetting(DashBoardUpdateRequest request) {

        MenuSettingRequest menuSetting = request.getMenuSetting();
        DashBoardRetrieveElement menuEdit = menuSetting.getMenuEdit();
        update(MENU_EDIT_URL,menuEdit.getOption());

        AccountManageRequest accountManage = request.getAccountManage();

        DashBoardRetrieveElement accountList = accountManage.getAccountList();
        update(ACCOUNT_MANAGE_URL,accountList.getOption());

        DashBoardRetrieveElement accountPolicy = accountManage.getAccountPolicy();
        update(ACCOUNT_POLICY_URL,accountPolicy.getOption());

        DashBoardRetrieveElement roles = accountManage.getRoles();
        update(ROLE_MANAGE_URL,roles.getOption());

        ContentManageRequest contentManage = request.getContentManage();

        DashBoardRetrieveElement post = contentManage.getPost();
        update(POST_MANAGE_URL,post.getOption());

        DashBoardRetrieveElement comment = contentManage.getComment();
        update(COMMENT_MANAGE_URL,comment.getOption());

        DashBoardRetrieveElement file = contentManage.getFile();
        update(FILE_MANAGE_URL,file.getOption());

        DashBoardRetrieveElement trash = contentManage.getTrash();
        update(TRASH_URL,trash.getOption());


        GeneralSettingRequest generalSetting = request.getGeneralSetting();

        DashBoardRetrieveElement general = generalSetting.getGeneral();
        update(GENERAL_URL,general.getOption());

        DashBoardRetrieveElement mainPage = generalSetting.getMainPage();
        update(MAIN_PAGE_MENU_MANAGE_URL,mainPage.getOption());
    }
}
