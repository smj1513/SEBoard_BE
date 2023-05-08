package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface CommentSearchRepository extends Repository<Comment, Long> {
    Page<Comment> findCommentListByPostId(Long postId, Pageable pagingInfo);
    List<Reply> findReplyListByCommentId(Long commentId);
    int countReplyByPostId(Long postId);
}
