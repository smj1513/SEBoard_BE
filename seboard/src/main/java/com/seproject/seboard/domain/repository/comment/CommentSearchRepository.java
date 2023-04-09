package com.seproject.seboard.domain.repository.comment;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;

import java.util.List;

public interface CommentSearchRepository {
    Page<Comment> findCommentByPostId(Long postId, PagingInfo pagingInfo);
    Page<Reply> findReplyByCommentId(Long commentId, PagingInfo pagingInfo);
    List<Reply> findReplyByCommentId(Long commentId);
}
