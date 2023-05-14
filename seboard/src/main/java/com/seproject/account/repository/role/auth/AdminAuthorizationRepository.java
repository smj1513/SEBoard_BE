package com.seproject.account.repository.role.auth;

import com.seproject.account.model.role.auth.AdminAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAuthorizationRepository extends JpaRepository<AdminAuthorization,Long> {

}
