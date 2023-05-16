package com.seproject.account.repository.token;

import com.seproject.account.model.token.LogoutRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutRefreshTokenRepository extends CrudRepository<LogoutRefreshToken,String> {
}
