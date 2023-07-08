package com.seproject.board.comment.domain.repository;

import com.seproject.board.comment.domain.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {
    @Query("select r from Reply r where r.post.postId = :postId" )
    List<Reply> findByPostId(Long postId);
}
