package com.seproject.seboard.application;

import com.seproject.account.model.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.DuplicatedReportException;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.domain.model.comment.Comment;
import com.seproject.seboard.domain.model.common.Report;
import com.seproject.seboard.domain.model.common.ReportThreshold;
import com.seproject.seboard.domain.model.post.Post;
import com.seproject.seboard.domain.model.user.Member;
import com.seproject.seboard.domain.repository.comment.CommentRepository;
import com.seproject.seboard.domain.repository.post.PostRepository;
import com.seproject.seboard.domain.repository.report.ReportRepository;
import com.seproject.seboard.domain.repository.report.ReportThresholdRepository;
import com.seproject.seboard.domain.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportAppService {
    private final ReportRepository reportRepository;
    private final ReportThresholdRepository reportThresholdRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
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

    public void reportPost(Long postId) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        Member member = memberRepository.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_POST));

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

    public void reportComment(Long commentId) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        Member member = memberRepository.findByAccountId(account.getAccountId())
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_MEMBER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_COMMENT));

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

    public void setReportThreshold(int threshold, String thresholdType) {
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));


        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

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
