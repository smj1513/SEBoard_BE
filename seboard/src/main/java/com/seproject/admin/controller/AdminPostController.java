package com.seproject.admin.controller;


import com.seproject.account.service.AccountService;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.application.dto.post.PostCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.controller.dto.post.PostRequest.*;
import static com.seproject.seboard.controller.dto.post.PostResponse.*;
import static com.seproject.seboard.application.dto.post.PostCommand.*;

@Tag(name = "게시글 관리 API", description = "관리자 시스템의 게시글 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminPostController {

    private final PostAppService postAppService;
    private final JwtDecoder jwtDecoder;
    private final AccountService accountService;

    @Operation(summary = "게시글 목록 조회", description = "등록된 게시글 목록들을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostListResponse.class)), responseCode = "200", description = "게시글 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "200", description = "잘못된 페이징 정보")
    })
    @GetMapping("/posts")
    public ResponseEntity<?> retrieveAllPost(@RequestBody RetrievePostListRequest retrievePostListRequest) {
//
//        PostListFindCommand command = PostCommand.PostListFindCommand.builder()
//                .categoryId(retrievePostListRequest.getCategoryId())
//                .page(retrievePostListRequest.getPage())
//                .size(retrievePostListRequest.getPerPage())
//                .build();
//
//        RetrievePostListResponse postList = postAppService.findPostList(command, false);
//
//        return new ResponseEntity<>(postList, HttpStatus.OK);
        return null;
    }

//    //TODO: 예외 추가
//    @Operation(summary = "게시글 삭제", description = "관리자는 게시글을 삭제한다.")
//    @ApiResponses({
//            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostListResponse.class)), responseCode = "200", description = "게시글 삭제 성공"),
//            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "200", description = "잘못된 페이징 정보")
//    })
//    @DeleteMapping("/posts/{postId}")
//    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
//        User user = (User)authentication.getPrincipal();
//        String username = user.getUsername();
//        Account account = accountService.(username);
//
//        postAppService.removePost(postId,account.getAccountId());
//        return new ResponseEntity<>("게시글 삭제 성공", HttpStatus.OK);
//    }
}
