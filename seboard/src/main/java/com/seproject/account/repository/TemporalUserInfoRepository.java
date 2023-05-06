package com.seproject.account.repository;

import com.seproject.account.model.social.TemporalUserInfo;
import org.springframework.data.repository.CrudRepository;

public interface TemporalUserInfoRepository extends CrudRepository<TemporalUserInfo,String> {
}
