package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.author.Anonymous;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousRepository extends JpaRepository<Anonymous,Long> {
}
