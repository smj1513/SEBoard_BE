package com.seproject.seboard.persistence;

import com.seproject.seboard.controller.dto.comment.CommentResponse;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentSearchJpaRepository extends CommentSearchRepository {


    @Query(value = "select new com.seproject.seboard.controller.dto.comment.CommentResponse$RetrieveCommentProfileElement(c)" +
            "from Comment c join Member m on c.author.boardUserId=m.boardUserId where c.author.account.loginId = :loginId and c.isOnlyReadByAuthor = false and c.status= 'NORMAL'",
    countQuery = "select count(c) from Comment c join Member m on c.author.boardUserId=m.boardUserId where c.author.account.loginId = :loginId and c.isOnlyReadByAuthor = false and c.status= 'NORMAL'")
    Page<CommentResponse.RetrieveCommentProfileElement> findMemberCommentByLoginId(String loginId, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.comment.CommentResponse$RetrieveCommentProfileElement(c) from Comment c where c.author.account.loginId = :loginId and c.status= 'NORMAL'",
    countQuery = "select count(c) from Comment c where c.author.account.loginId = :loginId and c.status= 'NORMAL'")
    Page<CommentResponse.RetrieveCommentProfileElement> findCommentByLoginId(String loginId, Pageable pagingInfo);
    @Query("select count(c) from Comment c where c.author.account.loginId = :loginId and c.status = 'NORMAL'")
    Integer countsCommentByLoginId(String loginId);
    @Query(value = "select * from comments as c where c.post_id = :postId and c.comment_type='comment' order by c.created_at asc", nativeQuery = true)
    Page<Comment> findCommentListByPostId(Long postId, Pageable pagingInfo);
    @Query("select r from Reply r where r.superComment.commentId = :commentId order by r.baseTime.createdAt asc")
    List<Reply> findReplyListByCommentId(Long commentId);
    @Query("select count(r) from Reply r where r.post.postId = :postId and r.status = 'NORMAL'")
    int countReplyByPostId(Long postId);
}
