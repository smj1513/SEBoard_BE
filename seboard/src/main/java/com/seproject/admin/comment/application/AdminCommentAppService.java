package com.seproject.admin.comment.application;

import com.seproject.admin.comment.controller.condition.AdminCommentRetrieveCondition;
import com.seproject.admin.comment.controller.dto.CommentDTO;
import com.seproject.admin.comment.controller.dto.CommentDTO.AdminDeletedCommentResponse;
import com.seproject.admin.comment.persistence.AdminCommentSearchJpaRepository;
import com.seproject.board.comment.service.CommentService;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.common.Status;
import com.seproject.board.common.domain.repository.ReportRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomIllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommentAppService {

    private final CommentRepository commentRepository;
    private final AdminCommentSearchJpaRepository adminCommentSearchRepository;
    private final ReportRepository reportRepository;

    private final CommentService commentService;


    public Page<AdminDeletedCommentResponse> retrieveDeletedCommentList(Pageable pageable){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities()
//                .stream().anyMatch(authority -> authority.getAuthority().equals(Role.ROLE_ADMIN));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        return adminCommentSearchRepository.findDeletedCommentList(pageable);
    }

    public Page<CommentDTO.AdminCommentListResponse> retrieveCommentList(AdminCommentRetrieveCondition condition, Pageable pageable){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        return adminCommentSearchRepository.findCommentListByCondition(condition, pageable);
    }

    @Transactional
    public void restoreComment(Long commentId){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        Comment comment = commentService.findById(commentId);

        comment.restore();
        reportRepository.deleteAllByCommentId(comment.getCommentId());
    }

    @Transactional
    public void restoreBulkComment(List<Long> commentIds) {
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        //N+1 문제
//        comments.forEach(comment -> {
//            comment.restore();
//            reportRepository.deleteAllByCommentId(comment.getCommentId());
//        });

        List<Comment> comments = commentService.findAllByIds(commentIds);

        if (comments.size() != commentIds.size())
            throw new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_COMMENT,null);

        reportRepository.deleteAllByCommentId(commentIds);
        commentService.updateStatus(commentIds, Status.NORMAL);
    }

    @Transactional
    public void deleteBulkComment(List<Long> commentIds, boolean isPermanent){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

//        //N+1 문제
//        //delete 함수 bulk update로 차라리 이런거 보면 그냥 hard delete하는게 더 마음 편할듯
//        commentRepository.findAllById(commentIds).forEach(comment -> {
//            comment.delete(isPermanent);
//        });
        List<Comment> comments = commentService.findAllByIds(commentIds);
        if (comments.size() != commentIds.size())
            throw new CustomIllegalArgumentException(ErrorCode.NOT_EXIST_COMMENT,null);

        Status status = isPermanent ? Status.PERMANENT_DELETED : Status.TEMP_DELETED;
        commentService.updateStatus(commentIds, status);
    }

}
