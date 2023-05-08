package com.seproject.account.repository.token;

import com.seproject.account.model.token.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
