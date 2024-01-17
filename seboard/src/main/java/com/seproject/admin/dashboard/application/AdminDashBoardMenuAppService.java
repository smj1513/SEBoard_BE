package com.seproject.admin.dashboard.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.role.service.RoleService;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.dashboard.controller.dto.DashBoardDTO;
import com.seproject.admin.dashboard.domain.DashBoardMenu;
import com.seproject.admin.dashboard.domain.DashBoardMenuAuthorization;
import com.seproject.admin.dashboard.service.AdminDashBoardService;
import com.seproject.admin.domain.SelectOption;
import com.seproject.admin.menu.controller.dto.MenuDTO;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.seproject.admin.dashboard.controller.dto.DashBoardDTO.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashBoardMenuAppService {

    private final AdminDashBoardService adminDashBoardService;
    private final RoleService roleService;

    public DashBoardMenuResponse findDashBoardMenus() {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN, null));

        List<Long> ids = adminDashBoardService.findAuthorizeDashBoardMenuIds(account);
        List<DashBoardMenu> dashBoardMenu = adminDashBoardService.findDashBoardMenu(ids);

        DashBoardMenuResponse response = DashBoardMenuResponse.toDTO(dashBoardMenu);

        return response;
    }

    public DashBoardMenuAuthorizationResponse findDashBoardMenuOptions() {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN, null));

        List<Long> ids = adminDashBoardService.findAuthorizeDashBoardMenuIds(account);
        List<DashBoardMenu> dashBoardMenus = adminDashBoardService.findDashBoardMenuWithRole(ids);

        DashBoardMenuAuthorizationResponse response = DashBoardMenuAuthorizationResponse.toDTO(dashBoardMenus);
        return response;
    }

    @Transactional
    public void update(DashBoardUpdateRequest request) {
        Long id = request.getId();
        MenuDTO.MenuAuthOption option = request.getOption();

        DashBoardMenu dashBoardMenu = adminDashBoardService.findDashBoardMenu(id)
                .orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_DASHBOARDMENU, null));
        SelectOption selectOption = SelectOption.of(option.getOption());
        List<Role> roles = parseRoles(option);
        adminDashBoardService.update(selectOption, dashBoardMenu,roles);
    }

    protected List<Role> parseRoles(MenuDTO.MenuAuthOption option) {
        SelectOption selectOption = SelectOption.of(option.getOption());
        if(selectOption == SelectOption.SELECT) {
            List<Long> roleIds = option.getRoles();
            return roleService.findByIds(roleIds);
        }

        return roleService.convertRoles(selectOption);
    }




}
