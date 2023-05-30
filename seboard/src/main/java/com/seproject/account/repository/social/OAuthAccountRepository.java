package com.seproject.account.repository.social;

import com.seproject.account.model.account.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {

    @Query("select account from OAuthAccount account where account.loginId = :loginId and account.status = 'NORMAL'")
    Optional<OAuthAccount> findByLoginId(String loginId);

    @Query("select account from OAuthAccount account where account.sub = :sub and account.provider = :provider and account.status = 'NORMAL'")
    Optional<OAuthAccount> findOAuthAccountBySubAndProvider(String sub,String provider);
}
