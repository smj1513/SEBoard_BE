package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.UnnamedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnnamedPostRepository extends JpaRepository<UnnamedPost, Long> {
}
