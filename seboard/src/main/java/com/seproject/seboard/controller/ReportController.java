package com.seproject.seboard.controller;

import com.seproject.seboard.application.ReportAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.report.ReportThresholdRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportAppService reportAppService;
    @PostMapping("/report/threshold")
    public ResponseEntity<MessageResponse> setReportThreshold(@RequestBody ReportThresholdRequest request) {
        reportAppService.setReportThreshold(request.getThreshold(), request.getThresholdType());
        return ResponseEntity.ok(MessageResponse.of("신고 임계치 설정 완료"));
    }
}
