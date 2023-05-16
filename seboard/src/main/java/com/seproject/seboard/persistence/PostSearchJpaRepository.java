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
            "from Post p where (p.category.menuId = :categoryId or p.category.superMenu.menuId = :categoryId) and p.pined = true and p.isDeleted = false order by p.baseTime.createdAt desc")
    List<RetrievePostListResponseElement> findPinedPostByCategoryId(Long categoryId);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where (p.category.menuId = :categoryId or p.category.superMenu.menuId = :categoryId) and p.isDeleted = false order by p.baseTime.createdAt desc",
            countQuery = "select count(p) from Post p where (p.category.menuId = :categoryId or p.category.superMenu.menuId = :categoryId) and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findPostByCategoryId(Long categoryId, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where p.title like %:title% and p.isDeleted = false order by p.baseTime.createdAt desc",
    countQuery = "select count(p) from Post p where p.title like %:title% and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findByTitle(String title, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where p.contents like %:content% and p.isDeleted = false order by p.baseTime.createdAt desc",
    countQuery = "select count(p) from Post p where p.contents like %:content% and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findByContents(String content, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where (p.title like %:query% or p.contents like %:query%) and p.isDeleted = false order by p.baseTime.createdAt desc",
    countQuery = "select count(p) from Post p where (p.title like %:query% or p.contents like %:query%) and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findByTitleOrContents(String query, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where p.author.name like %:authorName% and p.isDeleted = false order by p.baseTime.createdAt desc",
    countQuery = "select count(p) from Post p where p.author.name like %:authorName% and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findByAuthorName(String authorName, Pageable pagingInfo);
    @Query(value = "select new com.seproject.seboard.controller.dto.post.PostResponse$RetrievePostListResponseElement(p)" +
            "from Post p where (p.title like %:searchQuery% or p.contents like %:searchQuery% or p.author.name like %:searchQuery%) and p.isDeleted = false order by p.baseTime.createdAt desc",
    countQuery = "select count(p) from Post p where (p.title like %:searchQuery% or p.contents like %:searchQuery% or p.author.name like %:searchQuery%) and p.isDeleted = false")
    Page<RetrievePostListResponseElement> findByAllOptions(String searchQuery, Pageable pagingInfo);
}
