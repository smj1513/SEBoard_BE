package com.seproject.oauth2.repository;

import com.seproject.oauth2.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByLoginId(String loginId);
}
