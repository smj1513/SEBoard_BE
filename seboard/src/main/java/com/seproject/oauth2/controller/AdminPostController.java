package com.seproject.oauth2.controller;


import com.seproject.oauth2.controller.dto.RoleDTO;
import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.application.dto.post.PostCommand;
import com.seproject.seboard.controller.dto.post.PostResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.seproject.seboard.controller.dto.post.PostRequest.*;
import static com.seproject.seboard.controller.dto.post.PostResponse.*;
import static com.seproject.seboard.application.dto.post.PostCommand.*;

@Tag(name = "게시글 관리 API", description = "관리자 시스템의 게시글 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminPostController {

    private final PostAppService postAppService;

    @Operation(summary = "게시글 목록 조회", description = "등록된 권한 목록들을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RoleDTO.RetrieveAllRoleResponse.class)), responseCode = "200", description = "권한 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 페이징 정보")
    })
    @GetMapping("/posts")
    public ResponseEntity<?> retrieveAllRole(HttpServletRequest request, @RequestBody RetrievePostListRequest retrievePostListRequest) {

        PostListFindCommand command = PostCommand.PostListFindCommand.builder()
                .categoryId(retrievePostListRequest.getCategoryId())
                .page(retrievePostListRequest.getPage())
                .size(retrievePostListRequest.getPerPage())
                .build();

        RetrievePostListResponse postList = postAppService.findPostList(command, false);

        return new ResponseEntity<>(postList, HttpStatus.OK);
    }
}
