package com.seproject.seboard.domain.repository.user;

import com.seproject.seboard.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
