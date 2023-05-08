package com.seproject.account.repository.email;

import com.seproject.account.model.email.AccountRegisterConfirmedEmail;
import org.springframework.data.repository.CrudRepository;

public interface AccountRegisterConfirmedEmailRepository
        extends CrudRepository<AccountRegisterConfirmedEmail,String> {

    boolean existsByEmail(String email);
}
