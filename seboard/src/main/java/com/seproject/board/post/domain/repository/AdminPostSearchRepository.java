package com.seproject.board.post.domain.repository;

import com.seproject.board.post.controller.dto.AdminPostRequest.AdminPostRetrieveCondition;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminDeletedPostResponse;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminPostRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdminPostSearchRepository{
    Page<AdminPostRetrieveResponse> findPostListByCondition(AdminPostRetrieveCondition condition, Pageable pageable);
    Page<AdminDeletedPostResponse> findDeletedPostList(Pageable pageable);
}
