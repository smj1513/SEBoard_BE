package com.seproject.account.repository;

import com.seproject.account.model.EmailAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailAuthRepository extends JpaRepository<EmailAuthentication,Long> {

    @Query(value = "select * from email_authentication where email = :email and auth_token = :authToken",nativeQuery = true)
    EmailAuthentication findEmailAuthentication(String email, String authToken);

    @Query(value = "select * from email_authentication where email = :email",nativeQuery = true)
    EmailAuthentication findByEmail(String email);

    boolean existsByEmail(String email);
}
