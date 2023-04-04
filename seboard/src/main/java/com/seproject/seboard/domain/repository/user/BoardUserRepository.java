package com.seproject.seboard.domain.repository.user;

import com.seproject.seboard.domain.model.user.BoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
}
