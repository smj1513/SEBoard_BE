package com.seproject.board.menu.domain.repository;

import com.seproject.board.menu.domain.ExternalSiteMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalSiteMenuRepository extends JpaRepository<ExternalSiteMenu, Long> {
}
