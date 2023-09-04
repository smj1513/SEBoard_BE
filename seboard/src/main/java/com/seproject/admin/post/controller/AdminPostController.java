package com.seproject.admin.post.controller;


import com.seproject.admin.post.controller.dto.PostRequest.AdminPostRetrieveCondition;
import com.seproject.admin.post.controller.dto.PostRequest.BulkPostRequest;
import com.seproject.admin.post.controller.dto.PostRequest.MigratePostRequest;
import com.seproject.admin.post.controller.dto.PostResponse;
import com.seproject.admin.post.controller.dto.PostResponse.DeletedPostResponse;
import com.seproject.admin.post.application.AdminPostAppService;
import com.seproject.board.common.controller.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.admin.post.controller.dto.PostResponse.*;

@Tag(name = "게시글 관리 API", description = "관리자 시스템의 게시글 관리 API")
@RequestMapping(value = "/admin/posts")
@RestController
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostAppService adminPostAppService;

    @Operation(summary = "게시글 목록 조회", description = "등록된 게시글 목록들을 조회한다.")
    @GetMapping
    public ResponseEntity<Page<PostRetrieveResponse>> retrieveAllPost(@ModelAttribute AdminPostRetrieveCondition request,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "25") int perPage) {
        Page<PostRetrieveResponse> response = adminPostAppService.findPostList(request, page, perPage);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{postId}/restore")
    public ResponseEntity<MessageResponse> restorePost(@PathVariable Long postId){
        adminPostAppService.restorePost(postId);
        return new ResponseEntity<>(MessageResponse.of("게시글 복구 성공"), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<MessageResponse> restoreBulkPost(@RequestBody BulkPostRequest request){
        adminPostAppService.restoreBulkPost(request.getPostIds());
        return new ResponseEntity<>(MessageResponse.of("게시글 복구 성공"), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<MessageResponse> deleteBulkPost(@RequestBody BulkPostRequest request){
        adminPostAppService.deleteBulkPost(request.getPostIds(), false);
        return new ResponseEntity<>(MessageResponse.of("게시글 삭제 성공"), HttpStatus.OK);
    }

    @DeleteMapping("/permanent")
    public ResponseEntity<MessageResponse> deleteBulkPostPermanent(@RequestBody BulkPostRequest request){
        adminPostAppService.deleteBulkPost(request.getPostIds(), true);
        return new ResponseEntity<>(MessageResponse.of("게시글 영구 삭제 성공"), HttpStatus.OK);
    }

    @GetMapping("/deleted")
    public ResponseEntity<Page<DeletedPostResponse>> retrieveDeletedPostList(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "25") int perPage) {
        Page<DeletedPostResponse> response = adminPostAppService.findDeletedPostList(PageRequest.of(page, perPage));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/migrate") //TODO : Migrate 말고 게시글 카테고리 변경 , category migration이랑 이름 겹침
    public ResponseEntity<MessageResponse> migratePost(@RequestBody MigratePostRequest request){
        adminPostAppService.migratePost(request.getFromCategoryId(), request.getToCategoryId());
        return ResponseEntity.ok(MessageResponse.of("게시글 전체 이동 성공"));
    }
}
