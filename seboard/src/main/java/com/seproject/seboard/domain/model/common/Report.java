package com.seproject.seboard.domain.model.common;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@NoArgsConstructor
@Table(name = "reports")
public class Report {
    @Id @GeneratedValue
    private Long reportId;
    private Long targetId; //post or comment pk
    private Long memberId;
    private ReportType reportType;

    private Report(Long targetId, Long memberId, String reportType){
        this.targetId = targetId;
        this.memberId = memberId;
        this.reportType = ReportType.valueOf(reportType);
    }

    public static Report of(Long targetId, Long memberId, String reportType){
        return new Report(targetId, memberId, reportType);
    }
}
