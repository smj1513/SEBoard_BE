package com.seproject.seboard.persistence;

import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.comment.Reply;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentSearchJpaRepository implements CommentSearchRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Comment> findCommentByPostId(Long postId, PagingInfo pagingInfo) {
        List<Comment> data = em.createQuery("select c from Comment c where c.post.postId = :postId order by c.baseTime.createdAt desc", Comment.class)
                .setParameter("postId", postId)
                .setFirstResult(pagingInfo.getOffset())
                .setMaxResults(pagingInfo.getPerPage())
                .getResultList();

        int count = em.createQuery("select count(c) from Comment c where c.post.postId = :postId", Long.class)
                .setParameter("postId", postId)
                .getSingleResult().intValue();

        return new Page<Comment>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public Page<Reply> findReplyByCommentId(Long commentId, PagingInfo pagingInfo) {
        List<Reply> data = em.createQuery("select r from Reply r where r.superComment.commentId = :commentId order by r.baseTime.createdAt desc", Reply.class)
                .setParameter("commentId", commentId)
                .setFirstResult(pagingInfo.getOffset())
                .setMaxResults(pagingInfo.getPerPage())
                .getResultList();

        int count = em.createQuery("select count(r) from Reply r where r.superComment.commentId = :commentId", Long.class)
                .setParameter("commentId", commentId)
                .getSingleResult().intValue();


        return new Page<Reply>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public List<Reply> findReplyByCommentId(Long commentId) {
        return em.createQuery("select r from Reply r where r.superComment.commentId = :commentId order by r.baseTime.createdAt desc", Reply.class)
                .setParameter("commentId", commentId)
                .getResultList();
    }
}
