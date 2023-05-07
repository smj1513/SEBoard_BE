package com.seproject.account.repository.social;

import com.seproject.account.model.social.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {

    boolean existsBySubAndProvider(String sub,String provider);

    @Query("select account from OAuthAccount account join fetch account.account where account.account.loginId = :loginId")
    Optional<OAuthAccount> findByLoginId(String loginId);

    @Query("select account from OAuthAccount account join fetch account.account where account.sub = :sub and account.provider = :provider")
    OAuthAccount findOAuthAccountBySubAndProvider(String sub,String provider);
}
