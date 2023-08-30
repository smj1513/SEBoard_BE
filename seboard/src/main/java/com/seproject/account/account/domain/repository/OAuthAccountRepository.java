package com.seproject.account.account.domain.repository;

import com.seproject.account.account.domain.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {

    @Query("select account from OAuthAccount account where account.loginId = :loginId and account.status = 'NORMAL'")
    Optional<OAuthAccount> findByLoginId(@Param("loginId") String loginId);

    @Query("select account from OAuthAccount account where account.sub = :sub and account.provider = :provider and account.status = 'NORMAL'")
    Optional<OAuthAccount> findOAuthAccountBySubAndProvider(@Param("sub") String sub,@Param("provider") String provider);
}
