package com.seproject.account.account.domain.repository;

import com.seproject.account.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("select a from Account a where a.loginId = :loginId and a.status != 'PERMANENT_DELETED'")
    Optional<Account> findByLoginId(@Param("loginId") String loginId);
}
