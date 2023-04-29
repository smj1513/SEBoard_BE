package com.seproject.oauth2.repository;

import com.seproject.oauth2.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorizationRepository extends JpaRepository<Authorization,Long> {

//    @Query(value = "select * from authorizations join authorization_metadata on authorizations.id = authorization_metadata.authorization_id order by priority desc", nativeQuery = true)
    @Query("select distinct data from Authorization data join fetch data.roleAuthorizations order by data.priority desc")
    List<Authorization> findAllAuthorization();
    Authorization findAuthorizationByPathAndMethod(String path, String method);

    boolean existsByPathAndMethod(String path,String method);
}
