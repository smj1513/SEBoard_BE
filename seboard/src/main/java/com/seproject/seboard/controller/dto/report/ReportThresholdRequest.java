package com.seproject.seboard.controller.dto.report;

import lombok.Data;

@Data
public class ReportThresholdRequest {
    private int threshold;
    private String thresholdType;
}
