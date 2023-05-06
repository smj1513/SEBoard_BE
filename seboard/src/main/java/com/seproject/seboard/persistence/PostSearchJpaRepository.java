package com.seproject.seboard.persistence;

import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostSearchJpaRepository extends PostSearchRepository {
    @Query("select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostDetailResponse(p)" +
            "from Post p where p.postId = :id and p.isDeleted = false")
    Optional<PostResponse.RetrievePostDetailResponse> findPostDetailById(Long id);
//    Page<Post> findByCategoryId(Long categoryId, Pageable pagingInfo);
//    Page<Post> findByTitle(String title, Pageable pagingInfo);
//    Page<Post> findByContent(String content, Pageable pagingInfo);
//    Page<Post> findByTitleOrContent(String title, Pageable pagingInfo);
//    Page<Post> findByAuthorName(String authorName, Pageable pagingInfo);
//    Page<Post> findByAllOptions(String searchQuery, Pageable pagingInfo);
}
