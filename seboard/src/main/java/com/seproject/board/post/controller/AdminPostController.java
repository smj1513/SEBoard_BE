package com.seproject.board.post.controller;


import com.seproject.account.account.service.AccountService;
import com.seproject.account.token.utils.JwtDecoder;
import com.seproject.board.post.controller.dto.AdminPostRequest.AdminPostRetrieveCondition;
import com.seproject.board.post.controller.dto.AdminPostRequest.BulkPostRequest;
import com.seproject.board.post.controller.dto.AdminPostRequest.MigratePostRequest;
import com.seproject.board.post.controller.dto.AdminPostResponse.AdminDeletedPostResponse;
import com.seproject.board.post.application.AdminPostAppService;
import com.seproject.board.post.application.PostAppService;
import com.seproject.board.common.controller.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.board.post.controller.dto.PostResponse.*;

@Tag(name = "게시글 관리 API", description = "관리자 시스템의 게시글 관리 API")
@RequestMapping(value = "/admin/posts")
@RestController
@RequiredArgsConstructor
public class AdminPostController {

    private final PostAppService postAppService;
    private final JwtDecoder jwtDecoder;
    private final AccountService accountService;
    private final AdminPostAppService adminPostAppService;

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

    @Operation(summary = "게시글 목록 조회", description = "등록된 게시글 목록들을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostListResponse.class)), responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "200", description = "잘못된 페이징 정보")
    })
    @GetMapping
    public ResponseEntity<?> retrieveAllPost(@ModelAttribute AdminPostRetrieveCondition request,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "25") int perPage) {
        return ResponseEntity.ok(adminPostAppService.findPostList(request, page, perPage));
    }

    @GetMapping("/deleted")
    public ResponseEntity<Page<AdminDeletedPostResponse>> retrieveDeletedPostList(@RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "25") int perPage) {
        return ResponseEntity.ok(adminPostAppService.findDeletedPostList(PageRequest.of(page, perPage)));
    }

    @PostMapping("/migrate")
    public ResponseEntity<MessageResponse> migratePost(@RequestBody MigratePostRequest request){
        adminPostAppService.migratePost(request.getFromCategoryId(), request.getToCategoryId());
        return ResponseEntity.ok(MessageResponse.of("게시글 전체 이동 성공"));
    }
}
