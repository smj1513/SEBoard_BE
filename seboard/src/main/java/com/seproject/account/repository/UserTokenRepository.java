package com.seproject.account.repository;

import com.seproject.account.model.social.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepository extends CrudRepository<UserToken,String> {
}
