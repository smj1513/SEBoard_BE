package com.seproject.admin.controller;

import com.seproject.admin.controller.dto.comment.AdminCommentResponse;
import com.seproject.admin.controller.dto.comment.AdminCommentResponse.AdminCommentListResponse;
import com.seproject.admin.service.AdminCommentAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.admin.controller.dto.comment.AdminCommentRequest.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final AdminCommentAppService adminCommentAppService;

    @GetMapping
    public ResponseEntity<Page<AdminCommentListResponse>> retrieveCommentList(@ModelAttribute AdminCommentRetrieveCondition condition,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "25") int perPage){
        Page<AdminCommentListResponse> res = adminCommentAppService.retrieveCommentList(condition, PageRequest.of(page, perPage));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{commentId}/restore")
    public ResponseEntity<MessageResponse> restoreComment(@PathVariable Long commentId){
        adminCommentAppService.restoreComment(commentId);
        return ResponseEntity.ok(MessageResponse.of("댓글 복구 성공"));
    }

    @PostMapping("/restore")
    public ResponseEntity<MessageResponse> restoreBulkComment(@RequestBody BulkCommentRequest request){
        adminCommentAppService.restoreBulkComment(request.getCommentIds());
        return ResponseEntity.ok(MessageResponse.of("댓글 복구 성공"));
    }

    @DeleteMapping()
    public ResponseEntity<MessageResponse> deleteBulkComment(@RequestBody BulkCommentRequest request){
        adminCommentAppService.deleteBulkComment(request.getCommentIds(), false);
        return ResponseEntity.ok(MessageResponse.of("댓글 삭제 성공"));
    }

    @DeleteMapping("/permanent")
    public ResponseEntity<MessageResponse> deleteBulkCommentPermanent(@RequestBody BulkCommentRequest request){
        adminCommentAppService.deleteBulkComment(request.getCommentIds(), true);
        return ResponseEntity.ok(MessageResponse.of("댓글 영구 삭제 성공"));
    }

}
