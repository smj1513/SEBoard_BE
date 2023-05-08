package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostDetailResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.domain.model.post.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface PostSearchRepository extends Repository<Post, Long> {
    Optional<RetrievePostDetailResponse> findPostDetailById(Long id);
    List<RetrievePostListResponseElement> findPinedPostByCategoryId(Long categoryId);
    Page<RetrievePostListResponseElement> findPostByCategoryId(Long categoryId, Pageable pagingInfo);
//    Page<Post> findByCategoryId(Long categoryId, Pageable pagingInfo);
//    Page<Post> findByTitle(String title, Pageable pagingInfo);
//    Page<Post> findByContent(String content, Pageable pagingInfo);
//    Page<Post> findByTitleOrContent(String title, Pageable pagingInfo);
//    Page<Post> findByAuthorName(String authorName, Pageable pagingInfo);
//    Page<Post> findByAllOptions(String searchQuery, Pageable pagingInfo);
}

