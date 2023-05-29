package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostDetailResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.domain.model.post.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PostSearchRepository extends Repository<Post, Long> {
    Integer countsMemberPostByLoginId(String loginId);
    Integer countsPostByLoginId(String loginId);
    Page<RetrievePostListResponseElement> findBookmarkPostByLoginId(String loginId, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findMemberPostByLoginId(String loginId, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findPostByLoginId(String loginId, Pageable pagingInfo);
    Optional<RetrievePostDetailResponse> findPostDetailById(Long id);
    List<RetrievePostListResponseElement> findPinedPostByCategoryId(Long categoryId);
    Page<RetrievePostListResponseElement> findPostByCategoryId(Long categoryId, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findByTitle(String title, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findByContents(String content, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findByTitleOrContents(String title, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findByAuthorName(String authorName, Pageable pagingInfo);
    Page<RetrievePostListResponseElement> findByAllOptions(String searchQuery, Pageable pagingInfo);
}

