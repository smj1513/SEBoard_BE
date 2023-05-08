package com.seproject.seboard.persistence;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentSearchJpaRepository extends CommentSearchRepository {
    @Query("select c from Comment c where c.post.postId = :postId")
    Page<Comment> findCommentListByPostId(Long postId, Pageable pagingInfo);
    @Query("select r from Reply r where r.superComment.commentId = :commentId")
    List<Reply> findReplyListByCommentId(Long commentId);
    @Query("select count(r) from Reply r where r.post.postId = :postId")
    int countReplyByPostId(Long postId);
}
