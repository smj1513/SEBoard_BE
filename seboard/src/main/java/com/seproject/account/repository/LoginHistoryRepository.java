package com.seproject.account.repository;


import com.seproject.account.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {


    @Query("select count(loginHistory) from LoginHistory loginHistory where loginHistory.time between :startTime and :endTime")
    int countByTime(LocalDateTime startTime, LocalDateTime endTime);

}
