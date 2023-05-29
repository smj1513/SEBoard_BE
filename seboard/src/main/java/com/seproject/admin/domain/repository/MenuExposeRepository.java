package com.seproject.admin.domain.repository;

import com.seproject.account.model.role.Role;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.seboard.domain.model.category.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuExposeRepository extends JpaRepository<MenuAuthorization,Long> {


    @Query("select distinct menuExpose.menu from MenuAuthorization menuExpose where menuExpose.role.roleId in :roleIds")
    List<Menu> findAllByRoleId(List<Long> roleIds);

    @Query("select distinct menuExpose.role from MenuAuthorization menuExpose where menuExpose.menu.menuId = :menuId")
    List<Role> findAllRoleByMenuId(Long menuId);


    @Query("select distinct menu from Menu menu where menu.depth = 0 and menu.menuId not in (select menuExpose.menu from MenuAuthorization  menuExpose)")
    List<Menu> findAllNotExposeSetting();

    @Query("select distinct menuExpose from MenuAuthorization menuExpose where menuExpose.menu.menuId = :menuId")
    List<MenuAuthorization> findAllByMenuId(Long menuId);

}
