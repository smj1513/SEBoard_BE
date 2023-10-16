package com.seproject.board.common.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.role.domain.Role;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.board.comment.service.CommentService;
import com.seproject.board.post.service.PostService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.DuplicatedReportException;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.board.comment.domain.model.Comment;
import com.seproject.board.common.domain.Report;
import com.seproject.board.common.domain.ReportThreshold;
import com.seproject.board.post.domain.model.Post;
import com.seproject.member.domain.Member;
import com.seproject.board.comment.domain.repository.CommentRepository;
import com.seproject.board.post.domain.repository.PostRepository;
import com.seproject.board.common.domain.repository.ReportRepository;
import com.seproject.board.common.domain.repository.ReportThresholdRepository;
import com.seproject.member.domain.repository.MemberRepository;
import com.seproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportAppService {
    private final ReportRepository reportRepository;
    private final ReportThresholdRepository reportThresholdRepository;
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    private ReportThreshold postThreshold;
    private ReportThreshold commentThreshold;

    @PostConstruct
    protected void init(){
        postThreshold = reportThresholdRepository.findPostThreshold().orElseGet(()->
            ReportThreshold.of(5, "POST")
        );

        commentThreshold = reportThresholdRepository.findCommentThreshold().orElseGet(()->
            ReportThreshold.of(5, "COMMENT")
        );
    }

    @Transactional
    public void reportPost(Long postId) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        Member member = memberService.findByAccountId(account.getAccountId());

        Post post = postService.findById(postId);

        // 해당 사용자가 이전에 해당 post를 신고한 적이 있는지 확인
        // 신고한 적이 있다면 Exception 발생
        reportRepository.findByPostIdAndMemberId(postId, member.getBoardUserId())
                .ifPresent(report -> {
                    throw new DuplicatedReportException(ErrorCode.DUPLICATED_REPORT);
                });


        // 신고한 적이 없다면 report 테이블에 추가
        Report report = Report.of(postId, member.getBoardUserId(), "POST");
        reportRepository.save(report);

        // 신고한 적이 없다면 신고 횟수를 1 증가시키고, 신고 횟수가 threshold를 넘었는지 확인
        // threshold를 넘었다면 post 상태 변경
        post.increaseReportCount(postThreshold);
    }

    @Transactional
    public void reportComment(Long commentId) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        Member member = memberService.findByAccountId(account.getAccountId());

        Comment comment = commentService.findById(commentId);

        // 해당 사용자가 이전에 해당 post를 신고한 적이 있는지 확인
        // 신고한 적이 있다면 Exception 발생
        reportRepository.findByCommentIdAndMemberId(commentId, member.getBoardUserId())
                .ifPresent(report -> {
                    throw new DuplicatedReportException(ErrorCode.DUPLICATED_REPORT);
                });


        // 신고한 적이 없다면 report 테이블에 추가
        Report report = Report.of(commentId, member.getBoardUserId(), "COMMENT");
        reportRepository.save(report);

        // 신고한 적이 없다면 신고 횟수를 1 증가시키고, 신고 횟수가 threshold를 넘었는지 확인
        // threshold를 넘었다면 post 상태 변경
        comment.increaseReportCount(commentThreshold);
    }

    @Transactional //TODO : admin 이동
    public void setReportThreshold(int threshold, String thresholdType) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));


        boolean isAdmin = account.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.ROLE_ADMIN));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        if(thresholdType.equals("POST")){
            postThreshold = reportThresholdRepository.findPostThreshold().orElseGet(()->
                    ReportThreshold.of(threshold, thresholdType)
            );

            postThreshold.setThreshold(threshold);

            reportThresholdRepository.save(postThreshold);
        }
        else if(thresholdType.equals("COMMENT")){
            commentThreshold = reportThresholdRepository.findCommentThreshold().orElseGet(()->
                    ReportThreshold.of(threshold, thresholdType)
            );

            commentThreshold.setThreshold(threshold);

            reportThresholdRepository.save(commentThreshold);
        }
    }
}