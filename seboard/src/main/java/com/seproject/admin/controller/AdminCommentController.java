package com.seproject.admin.controller;

import com.seproject.admin.service.AdminCommentAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final AdminCommentAppService adminCommentAppService;

    @PostMapping("/{commentId}/restore")
    public ResponseEntity<MessageResponse> restoreComment(Long commentId){
        adminCommentAppService.restoreComment(commentId);
        return ResponseEntity.ok(MessageResponse.of("댓글 복구 성공"));
    }

}
