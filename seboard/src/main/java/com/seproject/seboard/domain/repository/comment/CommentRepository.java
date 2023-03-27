package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select count(comment) from Comment comment where comment.post.postId = :postId")
    int getCommentSizeByPostId(Long postId);

    @Query("select comment from Comment comment where comment.post.postId = :postId")
    ArrayList<Comment> findByPostId(@Param("postId") Long postId);
    //List<Comment> findRepliesBySuperCommentId(Long superCommentId);
}
