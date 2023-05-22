package com.seproject.admin.service;

import com.seproject.account.model.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommentAppService {
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

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

    public void deleteBulkComment(List<Long> commentIds){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.findAllById(commentIds).forEach(comment -> {
            comment.delete(false);
        });
    }
}
