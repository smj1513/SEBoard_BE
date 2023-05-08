package com.seproject.account.repository.email;

import com.seproject.account.model.email.AccountRegisterEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AccountRegisterEmailRepository extends CrudRepository<AccountRegisterEmail,String> {

//    @Query(value = "select * from email_authentication where email = :email and auth_token = :authToken",nativeQuery = true)
//    EmailAuthentication findEmailAuthentication(String email, String authToken);
//
//    @Query(value = "select * from email_authentication where email = :email",nativeQuery = true)
//    EmailAuthentication findByEmail(String email);
    boolean existsByEmail(String email);
}
