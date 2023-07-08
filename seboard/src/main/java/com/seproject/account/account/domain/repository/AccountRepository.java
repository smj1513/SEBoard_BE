package com.seproject.account.account.domain.repository;

import com.seproject.account.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("select a from Account a where a.loginId = :loginId and a.status = 'NORMAL'")
    Optional<Account> findByLoginId(String loginId);

    @Query(value = "select exists(select * from accounts where login_id = :loginId and status = 'NORMAL')",nativeQuery = true)
    boolean existsByLoginId(String loginId);

    @Query(value = "select exists(select * from accounts where nickname = :nickname and status = 'NORMAL')",nativeQuery = true)
    boolean existsByNickname(String nickname);
}
