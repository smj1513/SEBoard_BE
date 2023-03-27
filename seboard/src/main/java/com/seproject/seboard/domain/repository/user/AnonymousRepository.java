package com.seproject.seboard.domain.repository.user;

import com.seproject.seboard.domain.model.user.Anonymous;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousRepository extends JpaRepository<Anonymous,Long> {
}
