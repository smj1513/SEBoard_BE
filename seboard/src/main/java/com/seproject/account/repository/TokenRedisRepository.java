package com.seproject.account.repository;

import com.seproject.account.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}
