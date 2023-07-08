package com.seproject.board.common.controller.dto;

import lombok.Data;

@Data
public class ReportThresholdRequest {
    private int threshold;
    private String thresholdType;
}
