package com.seproject.seboard.persistence;

import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.domain.repository.post.PostSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostSearchJpaRepository extends PostSearchRepository {
    @Query("select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostDetailResponse(p)" +
            "from Post p where p.postId = :id and p.isDeleted = false")
    Optional<PostResponse.RetrievePostDetailResponse> findPostDetailById(Long id);
    @Query("select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where p.category.menuId = :categoryId and p.pined = true and p.isDeleted = false order by p.baseTime.createdAt desc")
    List<RetrievePostListResponseElement> findPinedPostByCategoryId(Long categoryId);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where p.category.menuId = :categoryId and p.isDeleted = false order by p.baseTime.createdAt desc",
            countQuery = "select count(p) from Post p where p.category.menuId = :categoryId and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findPostByCategoryId(Long categoryId, Pageable pagingInfo);
//    Page<Post> findByTitle(String title, Pageable pagingInfo);
//    Page<Post> findByContent(String content, Pageable pagingInfo);
//    Page<Post> findByTitleOrContent(String title, Pageable pagingInfo);
//    Page<Post> findByAuthorName(String authorName, Pageable pagingInfo);
//    Page<Post> findByAllOptions(String searchQuery, Pageable pagingInfo);
}
