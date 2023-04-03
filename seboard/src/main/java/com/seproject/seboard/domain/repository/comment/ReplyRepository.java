package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    List<Reply> findByPostId(Long postId);
}
