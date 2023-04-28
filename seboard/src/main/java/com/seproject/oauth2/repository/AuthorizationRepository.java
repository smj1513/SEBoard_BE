package com.seproject.oauth2.repository;

import com.seproject.oauth2.model.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorizationRepository extends JpaRepository<Authorization,Long> {

    @Query("select data from Authorization data join fetch data.roles order by data.priority desc")
    List<Authorization> findAllAuthorization();
}
