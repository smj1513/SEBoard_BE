package com.seproject.board.common.domain.repository;

import com.seproject.board.common.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.targetId = :postId and r.memberId = :memberId and r.reportType = 'POST'")
    Optional<Report> findByPostIdAndMemberId(Long postId, Long memberId);

    @Query("select r from Report r where r.targetId = :commentId and r.memberId = :memberId and r.reportType = 'COMMENT'")
    Optional<Report> findByCommentIdAndMemberId(Long commentId, Long memberId);

    @Modifying
    @Query("delete Report r where r.targetId = :postId and r.reportType = 'POST'")
    void deleteAllByPostId(Long postId);

    @Modifying //N+1
    @Query("delete Report r where r.targetId = :commentId and r.reportType = 'COMMENT'")
    void deleteAllByCommentId(@Param("commentId") Long commentId);

    @Modifying
    @Query("delete Report r where r.targetId in :commentIds and r.reportType = 'COMMENT'")
    void deleteAllByCommentId(@Param("commentIds") List<Long> commentIds);
}
