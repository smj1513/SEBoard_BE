package com.seproject.account.repository;

import com.seproject.account.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query("select a from Account a where a.loginId = :loginId and a.isDeleted = false")
    Optional<Account> findByLoginId(String loginId);

    @Query(value = "select exists(select * from accounts where login_id = :loginId and is_deleted = false)",nativeQuery = true)
    boolean existsByLoginId(String loginId);

    @Query(value = "select exists(select * from accounts where nickname = :nickname and is_deleted = false)",nativeQuery = true)
    boolean existsByNickname(String nickname);
}
