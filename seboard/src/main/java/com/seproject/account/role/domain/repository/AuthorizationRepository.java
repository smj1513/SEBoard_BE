package com.seproject.account.role.domain.repository;

import com.seproject.account.role.domain.Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorizationRepository extends JpaRepository<Authorization,Long> {

    @Query("select distinct data from Authorization data join fetch data.roleAuthorizations order by data.id desc ")
    List<Authorization> findAllAuthorization();

    @Query("select auth from Authorization auth where auth.path = :path")
    Optional<Authorization> findByPath(String path);
}
