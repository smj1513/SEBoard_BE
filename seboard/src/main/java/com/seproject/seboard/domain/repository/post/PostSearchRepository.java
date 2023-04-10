package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;

public interface PostSearchRepository {
    Page<Post> findPinedPostByCategoryId(Long categoryId, PagingInfo pagingInfo);
    Page<Post> findByCategoryId(Long categoryId, PagingInfo pagingInfo);
    Page<Post> findByTitle(String title, PagingInfo pagingInfo);
    Page<Post> findByContent(String content, PagingInfo pagingInfo);
    Page<Post> findByTitleOrContent(String title, PagingInfo pagingInfo);
    Page<Post> findByAuthorName(String authorName, PagingInfo pagingInfo);
    Page<Post> findByAllOptions(String searchQuery, PagingInfo pagingInfo);

}
