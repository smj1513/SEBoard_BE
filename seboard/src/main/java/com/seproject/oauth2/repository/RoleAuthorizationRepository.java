package com.seproject.oauth2.repository;

import com.seproject.oauth2.model.RoleAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAuthorizationRepository extends JpaRepository<RoleAuthorization,Long> {

}
