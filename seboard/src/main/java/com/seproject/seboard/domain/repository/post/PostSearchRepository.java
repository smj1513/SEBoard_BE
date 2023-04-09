package com.seproject.seboard.domain.repository.post;

import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.repository.Page;
import com.seproject.seboard.domain.repository.PagingInfo;

import java.util.List;

public interface PostSearchRepository {
    Page<Post> findPinedPostByCategoryId(Long categoryId, PagingInfo pagingInfo);
    Page<Post> findByCategoryId(Long categoryId, PagingInfo pagingInfo);
    Page<Post> findByTitle(String title, PagingInfo pagingInfo);
    Page<Post> findByContent(String content, PagingInfo pagingInfo);
    Page<Post> findByTitleAndContent(String title, PagingInfo pagingInfo);
//    //TODO : join 필요 -> 미구현됨
//    List<Post> findByAuthorName(String authorName);
//    //TODO : join 필요 -> 미구현됨
//    List<Post> findByAllOptions(String searchQuery);

}
