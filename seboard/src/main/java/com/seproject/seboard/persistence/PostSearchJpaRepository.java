package com.seproject.seboard.persistence;

import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostSearchJpaRepository implements PostSearchRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Post> findPinedPostByCategoryId(Long categoryId, PagingInfo pagingInfo) {
        List<Post> data = em.createQuery("select p from Post p where p.category.categoryId = :categoryId and p.pined = true", Post.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(pagingInfo.getOffset())
                .setMaxResults(pagingInfo.getPerPage())
                .getResultList();

        int count = em.createQuery("select count(p) from Post p where p.category.categoryId = :categoryId and p.pined = true", Long.class)
                .setParameter("categoryId", categoryId)
                .getSingleResult().intValue();

        return new Page<Post>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public Page<Post> findByCategoryId(Long categoryId, PagingInfo pagingInfo) {
        List<Post> data = em.createQuery("select p from Post p where p.category.categoryId = :categoryId order by p.baseTime.createdAt desc", Post.class)
                .setParameter("categoryId", categoryId)
                .setFirstResult(pagingInfo.getOffset())
                .setMaxResults(pagingInfo.getPerPage())
                .getResultList();

        int count = em.createQuery("select count(p) from Post p where p.category.categoryId = :categoryId", Long.class)
                .setParameter("categoryId", categoryId)
                .getSingleResult().intValue();

        return new Page<Post>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public Page<Post> findByTitle(String title, PagingInfo pagingInfo) {
        List<Post> data = em.createQuery("select p from Post p where p.title like %:title%", Post.class)
                .setParameter("title", title)
                .getResultList();

        int count = em.createQuery("select count(p) from Post p where p.title like %:title%", Long.class)
                .setParameter("title", title)
                .getSingleResult().intValue();

        return new Page<Post>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public Page<Post> findByContent(String content, PagingInfo pagingInfo) {
        List<Post> data = em.createQuery("select p from Post p where p.contents like %:content%", Post.class)
                .setParameter("content", content)
                .getResultList();

        int count = em.createQuery("select count(p) from Post p where p.contents like %:content%", Long.class)
                .setParameter("content", content)
                .getSingleResult().intValue();

        return new Page<Post>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }

    @Override
    public Page<Post> findByTitleAndContent(String query, PagingInfo pagingInfo) {
        List<Post> data = em.createQuery("select p from Post p where p.title like %:title% or p.contents like %:content%", Post.class)
                .setParameter("title", query)
                .setParameter("content", query)
                .getResultList();

        int count = em.createQuery("select count(p) from Post p where p.title like %:title% or p.contents like %:content%", Long.class)
                .setParameter("title", query)
                .setParameter("content", query)
                .getSingleResult().intValue();

        return new Page<>(pagingInfo.getCurPage(), pagingInfo.getPerPage(), count, data);
    }
}
