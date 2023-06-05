package com.seproject.admin.domain.repository;

import com.seproject.admin.controller.dto.comment.AdminCommentRequest;
import com.seproject.admin.controller.dto.comment.AdminCommentRequest.AdminCommentRetrieveCondition;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse.AdminCommentListResponse;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse.AdminDeletedCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCommentSearchRepository {
    Page<AdminCommentListResponse> findCommentListByCondition(AdminCommentRetrieveCondition condition, Pageable pageable);
    Page<AdminDeletedCommentResponse> findDeletedCommentList(Pageable pageable);
}
