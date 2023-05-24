package com.seproject.admin.service;

import com.seproject.account.model.role.Role;
import com.seproject.account.repository.role.RoleRepository;
import com.seproject.admin.domain.MenuExpose;
import com.seproject.admin.repository.MenuExposeRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import com.seproject.seboard.domain.model.category.Menu;
import com.seproject.seboard.domain.repository.category.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MenuExposeService {
    private final MenuExposeRepository menuExposeRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    public MenuExpose createMenuExpose(Long menuId,Long roleId){
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
        if(menu.getSuperMenu() != null) throw new CustomIllegalArgumentException(ErrorCode.CATEGORY_NOT_EXIST_EXPOSE_OPTION,null);
        MenuExpose menuExpose = MenuExpose.builder()
                .menu(menu)
                .role(role)
                .build();

        menuExposeRepository.save(menuExpose);

        return menuExpose;
    }

    @Transactional
    public MenuExpose updateRoleInMenuExpose(Long id,Long roleId){
        MenuExpose menuExpose = menuExposeRepository.findById(id).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
        menuExpose.changeRole(role);
        return menuExpose;
    }

    public List<Menu> retrieveMenuByRole(List<Role> authorities) {
        List<Long> roleIds = authorities.stream().map(Role::getId).collect(Collectors.toList());
        return menuExposeRepository.findAllByRoleId(roleIds);
    }

    public List<Role> retrieveMenuExposeByMenuId(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY, null));
        if(menu.getSuperMenu() != null) throw new CustomIllegalArgumentException(ErrorCode.CATEGORY_NOT_EXIST_EXPOSE_OPTION,null);

        List<Role> findRolesByMenuId = menuExposeRepository.findAllByMenuId(menuId);
        return findRolesByMenuId;
    }

    public MenuExpose deleteMenuExpose(Long id){
        MenuExpose menuExpose = menuExposeRepository.findById(id).orElseThrow(() -> new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_CATEGORY,null));
        menuExposeRepository.delete(menuExpose);
        return menuExpose;
    }
}
