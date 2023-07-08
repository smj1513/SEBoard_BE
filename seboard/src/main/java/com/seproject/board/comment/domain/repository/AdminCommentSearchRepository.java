package com.seproject.board.comment.domain.repository;

import com.seproject.board.comment.controller.dto.AdminCommentRequest.AdminCommentRetrieveCondition;
import com.seproject.board.comment.controller.dto.AdminCommentResponse.AdminCommentListResponse;
import com.seproject.board.comment.controller.dto.AdminCommentResponse.AdminDeletedCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminCommentSearchRepository {
    Page<AdminCommentListResponse> findCommentListByCondition(AdminCommentRetrieveCondition condition, Pageable pageable);
    Page<AdminDeletedCommentResponse> findDeletedCommentList(Pageable pageable);
}
