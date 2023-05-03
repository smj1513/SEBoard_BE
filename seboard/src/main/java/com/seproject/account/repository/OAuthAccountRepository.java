package com.seproject.account.repository;

import com.seproject.account.model.social.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {

    boolean existsBySubAndProvider(String sub,String provider);
    boolean existsBySub(String sub);

    @Query("select account from OAuthAccount account join fetch account.account where account.sub = :sub and account.provider = :provider")
    OAuthAccount findOAuthAccountBySubAndProvider(String sub,String provider);
}
