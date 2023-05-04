package com.seproject.account.repository;

import com.seproject.account.model.AccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
}
