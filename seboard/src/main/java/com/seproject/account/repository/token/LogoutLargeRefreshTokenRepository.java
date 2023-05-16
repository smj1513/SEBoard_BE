package com.seproject.account.repository.token;

import com.seproject.account.model.token.LogoutLargeRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutLargeRefreshTokenRepository extends CrudRepository<LogoutLargeRefreshToken,String> {
}
