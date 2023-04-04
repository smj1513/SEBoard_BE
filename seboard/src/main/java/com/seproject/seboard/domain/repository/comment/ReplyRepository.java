package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    @Query(value = "select * from replies where post_id = :postId", nativeQuery = true)
    List<Reply> findByPostId(Long postId);
}
