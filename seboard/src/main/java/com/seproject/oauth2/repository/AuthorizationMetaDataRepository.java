package com.seproject.oauth2.repository;

import com.seproject.oauth2.model.AuthorizationMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorizationMetaDataRepository extends JpaRepository<AuthorizationMetaData,Long> {

    List<AuthorizationMetaData> findByMethodSignature(String methodSignature);
}
