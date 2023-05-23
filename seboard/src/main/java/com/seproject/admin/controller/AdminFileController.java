package com.seproject.admin.controller;

import com.seproject.admin.controller.dto.file.FileRequest;
import com.seproject.admin.controller.dto.file.FileRequest.BulkFileRequest;
import com.seproject.admin.service.AdminFileAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin/files")
@RestController
@RequiredArgsConstructor
public class AdminFileController {
    private final AdminFileAppService adminFileAppService;
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteBulkFile(@RequestBody BulkFileRequest fileIds){
        adminFileAppService.deleteBulkFile(fileIds.getFileIds());
        return ResponseEntity.ok(MessageResponse.of("파일 삭제 완료"));
    }

}
