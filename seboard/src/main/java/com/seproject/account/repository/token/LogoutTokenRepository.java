package com.seproject.account.repository.token;

import com.seproject.account.model.token.LogoutToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutTokenRepository extends CrudRepository<LogoutToken,String> {
}
