package com.seproject.board.menu.domain.repository;

import com.seproject.board.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("select m from Menu m where m.depth = :depth order by m.menuId ASC ")
    List<Menu> findByDepth(int depth);
    @Query("select m from Menu m where m.superMenu.menuId = :superMenuId order by m.menuId ASC ")
    List<Menu> findBySuperMenu(Long superMenuId);
    @Query("select case when count(m) > 0 then true else false end from Menu m where m.superMenu.menuId = :menuId")
    boolean existsSubMenuById(Long menuId);


    @Query("select menu from Menu menu where menu.superMenu.menuId in :superMenuIds order by menu.menuId ASC ")
    List<Menu> findAllBySuperMenuIds(List<Long> superMenuIds);

    @Query("select distinct m from Menu m left outer join fetch m.menuAuthorizations auth where m.superMenu.menuId = :superMenuId order by m.menuId ASC")
    List<Menu> findBySuperMenuWithAuthorization(Long superMenuId);
}
