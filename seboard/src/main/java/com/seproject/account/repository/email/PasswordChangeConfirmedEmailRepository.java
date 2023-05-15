package com.seproject.account.repository.email;

import com.seproject.account.model.email.PasswordChangeConfirmedEmail;
import org.springframework.data.repository.CrudRepository;

public interface PasswordChangeConfirmedEmailRepository
        extends CrudRepository<PasswordChangeConfirmedEmail,String> {
}
