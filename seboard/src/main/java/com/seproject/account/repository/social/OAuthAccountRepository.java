package com.seproject.account.repository.social;

import com.seproject.account.model.social.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {

    @Query(value = "select exists(select * from accounts a join oauth_accounts b on a.account_id = b.account_id where is_deleted = false and sub = :sub and provider = :provider)",
            nativeQuery = true)
    boolean existsBySubAndProvider(String sub,String provider);

    @Query("select account from OAuthAccount account join fetch account.account where account.account.loginId = :loginId and account.account.isDeleted = false")
    Optional<OAuthAccount> findByLoginId(String loginId);

    @Query("select account from OAuthAccount account join fetch account.account where account.sub = :sub and account.provider = :provider and account.account.isDeleted = false")
    OAuthAccount findOAuthAccountBySubAndProvider(String sub,String provider);
}
