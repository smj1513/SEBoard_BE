package com.seproject.account.repository.email;

import com.seproject.account.model.email.AccountRegisterEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AccountRegisterEmailRepository extends CrudRepository<AccountRegisterEmail,String> {

}
