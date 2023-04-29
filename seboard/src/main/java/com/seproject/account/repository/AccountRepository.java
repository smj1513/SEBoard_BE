package com.seproject.account.repository;

import com.seproject.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
