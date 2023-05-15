package com.seproject.account.repository.email;

import com.seproject.account.model.email.PasswordChangeEmail;
import org.springframework.data.repository.CrudRepository;

public interface PasswordChangeEmailRepository extends CrudRepository<PasswordChangeEmail,String> {
}
