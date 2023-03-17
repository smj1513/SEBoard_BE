package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    int getCommentSizeByPostId(Long postId);
}
