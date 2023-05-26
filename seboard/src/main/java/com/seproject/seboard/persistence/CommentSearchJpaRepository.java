package com.seproject.seboard.persistence;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentSearchJpaRepository extends CommentSearchRepository {
//    @Query(nativeQuery = true, value =
//    "select count(*)\n" +
//            "from comments\n" +
//            "where comments.board_user_id=(\n" +
//            "select board_user_id\n" +
//            "from (\n" +
//            "(select board_user_id, account_id\n" +
//            "from board_users join members\n" +
//            "on board_users.board_user_id=members.member_id )\n" +
//            "UNION ALL\n" +
//            "(select board_user_id, account_id\n" +
//            "from board_users join anonymous\n" +
//            "on board_users.board_user_id=anonymous.anonymous_id\n" +
//            ")) u\n" +
//            "where u.account_id=(\n" +
//            "select account_id\n" +
//            "from accounts\n" +
//            "where accounts.login_id=:loginId\n" +
//            "))")
//    Integer countsCommentByLoginId(String loginId);


    @Query("select count(c) from Comment c where c.post.author.account.loginId = :loginId")
    Integer countsCommentByLoginId(String loginId);
//    @Query("select c from Comment c where c.post.postId = :postId order by c.baseTime.createdAt desc")
    @Query(value = "select * from comments as c where c.post_id = :postId and c.comment_type='comment' order by c.created_at asc", nativeQuery = true)
    Page<Comment> findCommentListByPostId(Long postId, Pageable pagingInfo);
    @Query("select r from Reply r where r.superComment.commentId = :commentId order by r.baseTime.createdAt asc")
    List<Reply> findReplyListByCommentId(Long commentId);
    @Query("select count(r) from Reply r where r.post.postId = :postId")
    int countReplyByPostId(Long postId);
}
