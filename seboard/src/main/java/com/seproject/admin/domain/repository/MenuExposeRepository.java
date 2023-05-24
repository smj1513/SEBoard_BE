package com.seproject.admin.repository;

import com.seproject.account.model.role.Role;
import com.seproject.admin.domain.MenuExpose;
import com.seproject.seboard.domain.model.category.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuExposeRepository extends JpaRepository<MenuExpose,Long> {


    @Query("select distinct menuExpose.menu from MenuExpose menuExpose where menuExpose.role.roleId in :roleIds")
    List<Menu> findAllByRoleId(List<Long> roleIds);

    @Query("select distinct menuExpose.role from MenuExpose menuExpose where menuExpose.menu.menuId = :menuId")
    List<Role> findAllByMenuId(Long menuId);


    @Query("select distinct menu from Menu menu where menu.depth = 0 and menu.menuId not in (select menuExpose.menu from MenuExpose  menuExpose)")
    List<Menu> findAllNotExposeSetting();
}
