package com.seproject.account.common.domain.repository;


import com.seproject.account.common.domain.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {


    @Query("select count(loginHistory) from LoginHistory loginHistory where loginHistory.time between :startTime and :endTime")
    int countByTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
