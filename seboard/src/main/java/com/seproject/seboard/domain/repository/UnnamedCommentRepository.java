package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.UnnamedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnnamedCommentRepository extends JpaRepository<UnnamedComment,Long> {

}
