package com.seproject.account.common.domain.repository;

import com.seproject.account.common.domain.LoginPreventUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LoginPreventUserRepository extends JpaRepository<LoginPreventUser,Long> {

    @Query(value = "select exists(select * from login_prevent_user where login_id = :loginId and local_date_time >= :now)", nativeQuery = true)
    boolean isPreventUser(String loginId, LocalDateTime now);
}
