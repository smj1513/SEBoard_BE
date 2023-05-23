package com.seproject.seboard.domain.repository.report;

import com.seproject.seboard.domain.model.common.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.targetId = :postId and r.memberId = :memberId and r.reportType = 'POST'")
    Optional<Report> findByPostIdAndMemberId(Long postId, Long memberId);

    @Query("select r from Report r where r.targetId = :commentId and r.memberId = :memberId and r.reportType = 'COMMENT'")
    Optional<Report> findByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    @Query("delete Report r where r.targetId = :postId and r.reportType = 'POST'")
    void deleteAllByPostId(Long postId);

    @Modifying
    @Query("delete Report r where r.targetId = :commentId and r.reportType = 'COMMENT'")
    void deleteAllByCommentId(Long commentId);
}
