package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select count(comment) from Comment comment where comment.post.postId = :postId")
    int countCommentsByPostId(Long postId);

    @Query("select comment from Comment comment where comment.post.postId = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    //List<Comment> findRepliesBySuperCommentId(Long superCommentId);

    @Query("select c from Comment c join c.author join c.author.account where c.author.account.accountId = :accountId")
    List<Comment> findCommentsByAccountId(Long accountId);
}
