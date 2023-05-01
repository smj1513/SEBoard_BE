package com.seproject.account.repository;

import com.seproject.account.model.RoleAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorization,Long> {

}
