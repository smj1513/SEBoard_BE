package com.seproject.admin.domain.repository;

import com.seproject.admin.controller.dto.post.AdminPostRequest;
import com.seproject.admin.controller.dto.post.AdminPostRequest.AdminPostRetrieveCondition;
import com.seproject.admin.controller.dto.post.AdminPostResponse;
import com.seproject.seboard.domain.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


public interface AdminPostSearchRepository{
    Page<AdminPostResponse> findPostListByCondition(AdminPostRetrieveCondition condition, Pageable pageable);
}
