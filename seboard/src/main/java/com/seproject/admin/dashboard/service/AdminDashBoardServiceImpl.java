package com.seproject.admin.dashboard.service;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.admin.dashboard.domain.DashBoardMenu;
import com.seproject.admin.dashboard.domain.DashBoardMenuAuthorization;
import com.seproject.admin.dashboard.domain.repository.DashBoardMenuRepository;
import com.seproject.admin.domain.SelectOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminDashBoardServiceImpl {
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


    private final DashBoardMenuRepository dashBoardMenuRepository;

    private List<DashBoardMenu> all; // cache

    public AdminDashBoardServiceImpl(DashBoardMenuRepository dashBoardMenuRepository) {
        this.dashBoardMenuRepository = dashBoardMenuRepository;
        all = dashBoardMenuRepository.findAll();
    }

    public Optional<DashBoardMenu> findDashBoardMenu(Long id) {
        return dashBoardMenuRepository.findById(id);
    }

    public List<DashBoardMenu> findDashBoardMenu(List<Long> ids) {
        List<DashBoardMenu> allById = dashBoardMenuRepository.findAllById(ids);
        return allById;
    }

    public List<DashBoardMenu> findDashBoardMenuWithRole(List<Long> ids) {
        List<DashBoardMenu> dashBoardMenusWithRole = dashBoardMenuRepository.findDashBoardMenusWithRole(ids);
        return dashBoardMenusWithRole;
    }

    public List<Long> findAuthorizeDashBoardMenuIds(Account account) {
        List<Role> roles = account.getRoles();
        List<Long> ids = new ArrayList<>();

        for (DashBoardMenu dashBoardMenu : all) {
            if(dashBoardMenu.authorize(roles)) {
                ids.add(dashBoardMenu.getId());
            }
        }

        return ids;
    }

    @Transactional
    public void update(SelectOption selectOption, DashBoardMenu dashBoardMenu, List<Role> roles) {

        List<DashBoardMenuAuthorization> collect = roles.stream()
                .map((role) -> new DashBoardMenuAuthorization(dashBoardMenu, role))
                .collect(Collectors.toList());

        dashBoardMenu.update(selectOption, collect);

        all = dashBoardMenuRepository.findAll();
    }

}
