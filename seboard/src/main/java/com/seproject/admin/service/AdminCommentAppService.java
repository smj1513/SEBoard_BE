package com.seproject.admin.service;

import com.seproject.account.model.account.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.controller.dto.comment.AdminCommentRequest;
import com.seproject.admin.controller.dto.comment.AdminCommentRequest.AdminCommentRetrieveCondition;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse.AdminCommentListResponse;
import com.seproject.admin.domain.repository.AdminCommentSearchRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.comment.CommentSearchRepository;
import com.seproject.seboard.domain.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentAppService {
    private final CommentRepository commentRepository;
    private final AdminCommentSearchRepository adminCommentSearchRepository;
    private final ReportRepository reportRepository;

    public Page<AdminCommentListResponse> retrieveCommentList(AdminCommentRetrieveCondition condition, Pageable pageable){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        return adminCommentSearchRepository.findCommentListByCondition(condition, pageable);
    }

    public void restoreComment(Long commentId){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

        comment.restore();
        reportRepository.deleteAllByCommentId(comment.getCommentId());
    }

    public void restoreBulkComment(List<Long> commentIds) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.findAllById(commentIds).forEach(comment -> {
            comment.restore();
            reportRepository.deleteAllByCommentId(comment.getCommentId());
        });
    }

    public void deleteBulkComment(List<Long> commentIds, boolean isPermanent){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.findAllById(commentIds).forEach(comment -> {
            comment.delete(isPermanent);
        });
    }

}
