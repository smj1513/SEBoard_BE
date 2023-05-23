package com.seproject.admin.controller;

import com.seproject.admin.controller.dto.file.FileRequest;
import com.seproject.admin.controller.dto.file.FileRequest.BulkFileRequest;
import com.seproject.admin.controller.dto.file.FileRequest.FileExtensionRequest;
import com.seproject.admin.controller.dto.file.FileResponse;
import com.seproject.admin.controller.dto.file.FileResponse.FileExtensionResponse;
import com.seproject.admin.service.AdminFileAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/extension")
    public ResponseEntity<MessageResponse> addAllowedFileExtension(@RequestBody FileExtensionRequest fileExtensionRequest){
        adminFileAppService.addFileExtension(fileExtensionRequest.getExtensions());
        return ResponseEntity.ok(MessageResponse.of("허용 확장자 추가 완료"));
    }

    @DeleteMapping("/extension")
    public ResponseEntity<MessageResponse> removeAllowedFileExtension(@RequestBody FileExtensionRequest fileExtensionRequest){
        adminFileAppService.removeFileExtension(fileExtensionRequest.getExtensions());
        return ResponseEntity.ok(MessageResponse.of("허용 확장자 삭제 완료"));
    }

    @GetMapping("/extension")
    public ResponseEntity<FileExtensionResponse> retrieveAllowedFileExtension(){
        FileExtensionResponse fileExtensionResponse = adminFileAppService.retrieveFileExtension();
        return ResponseEntity.ok(fileExtensionResponse);
    }
}
