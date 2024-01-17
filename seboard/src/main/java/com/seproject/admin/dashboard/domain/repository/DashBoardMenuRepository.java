package com.seproject.admin.dashboard.domain.repository;

import com.seproject.admin.dashboard.domain.DashBoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DashBoardMenuRepository extends JpaRepository<DashBoardMenu, Long> {

    Optional<DashBoardMenu> findDashBoardMenuByUrlInfo(String urlInfo);

}
