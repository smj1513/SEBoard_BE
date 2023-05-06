package com.seproject.seboard.controller;

import com.seproject.seboard.application.CommentAppService;
import com.seproject.seboard.application.PostAppService;
import com.seproject.seboard.application.PostSearchAppService;
import com.seproject.seboard.application.dto.comment.CommentCommand;
import com.seproject.seboard.application.dto.post.PostCommand.PostListFindCommand;
import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.comment.CommentResponse;
import com.seproject.seboard.controller.dto.post.PostRequest.CreatePostRequest;
import com.seproject.seboard.controller.dto.post.PostRequest.UpdateNamedPostRequest;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static com.seproject.seboard.controller.dto.comment.CommentResponse.*;
import static com.seproject.seboard.controller.dto.post.PostResponse.*;

@Slf4j
@Tag(name = "게시글 API", description = "게시글(posts) 관련 API")
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
    private final PostAppService postAppService;
    private final PostSearchAppService postSearchAppService;
    private final CommentAppService commentAppService;

    @Parameters(
            {
                    @Parameter(name = "categoryId", description = "조회하고자 하는 카테고리 pk"),
                    @Parameter(name = "page", description = "페이지 번호"),
                    @Parameter(name = "perPage", description = "페이지 당 게시글 개수"),
                    @Parameter(name = "pined", description = "상단 고정 글 포함 여부")
            }
    )
    @Operation(summary = "게시글 목록 조회", description = "카테고리, 페이징 정보를 전달하여 게시글 목록 조회한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostListResponseElement.class)), responseCode = "200", description = "상단 고정글 미 포함 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePinedPostListResponse.class)), responseCode = "200", description = "상단 고정글 포함 조회 성공"),
    })
    @GetMapping
    public ResponseEntity<?> retrievePostList(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "25") Integer perPage,
            @RequestParam(defaultValue = "true") Boolean pined
    ) {
//        if(pined){
//            RetrievePostListResponse pinedList = postAppService.findPostList(
//                    PostListFindCommand.builder()
//                            .categoryId(categoryId)
//                            .page(page)
//                            .size(perPage)
//                            .build(),
//                    true
//            );
//
//            RetrievePostListResponse normalList = postAppService.findPostList(
//                    PostListFindCommand.builder()
//                            .categoryId(categoryId)
//                            .page(page)
//                            .size(perPage-pinedList.getPaginationInfo().getContentSize())
//                            .build(),
//                    false
//            );
//
//            return new ResponseEntity<>(RetrievePinedPostListResponse.toDTO(pinedList, normalList), HttpStatus.OK);
//        }else{
//            RetrievePostListResponse retrievePostListResponse = postAppService.findPostList(
//                    PostListFindCommand.builder()
//                            .categoryId(categoryId)
//                            .page(page)
//                            .size(perPage)
//                            .build(),
//                    true
//            );
//
//            return new ResponseEntity<>(retrievePostListResponse, HttpStatus.OK);



        /***
         *  TODO:  페이지 번호 0일때
         *          페이지 번호 MAX 이상
         *          category id가 없는 경우, 설정 안된경우
         */
        return null;
    }

    @Parameter(name = "postId", description = "상세 조회를 할 게시물의 pk")
    @Operation(summary = "게시글 상세 조회", description = "게시글을 클릭하면 게시글의 상세 내역을 조회한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostDetailResponse.class)), responseCode = "200", description = "조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = MessageResponse.class)), responseCode = "404", description = "해당 pk를 가진 게시물이 없음")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> retrievePost(@PathVariable("postId") Long postId) { // TODO : accountId는 jwt에서 추출

        Long accountId = 5234058023853L;
        /***
         * TODO : jwt 추가
         *      없는 postId를 조회
         */

        try {
            RetrievePostDetailResponse postDetailRes = postSearchAppService.findPostDetail(postId, accountId);
            return new ResponseEntity<>(postDetailRes, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            MessageResponse response = MessageResponse.of(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @Parameter(name = "request", description = "게시물 생성에 필요한 제목, 본문, 공개여부, 익명 여부, 첨부파일, 카테고리 pk, 상단 고정 여부 정보")
    @Operation(summary = "게시글 작성", description = "사용자는 실명으로 게시글을 작성한다")
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) { //TODO : accountId 어떻게?
        Long accountId = 5234058023853L;

        postAppService.writePost(request.toCommand(accountId));

        //TODO : 실패시, 예외상황 추가 필요
        return new ResponseEntity<>(MessageResponse.of("작성 성공"), HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "postId", description = "수정할 게시글 pk"),
                    @Parameter(name = "request", description = "게시물 수정에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 공지사항 여부 정보")
            }
    )
    @Operation(summary = "게시글 수정", description = "사용자는 본인이 실명으로 작성한 게시물을 수정한다")
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody UpdateNamedPostRequest request) { //TODO : 첨부파일 필드 추가
        Long accountId = 5234058023853L; //TODO : accountId 어떻게?

        /**
         * TODO : required 필드 null 체크
         *    권한 처리
         *    존재하지 않는 categoryId
         *    제목 또는 본문이 비어있거나 길이 초과
         */
        postAppService.editPost(
                request.toCommand(postId, accountId)
        );

        return new ResponseEntity<>(MessageResponse.of(""), HttpStatus.OK);
    }

    @Parameter(name = "postId", description = "삭제할 게시글의 pk")
    @Operation(summary = "게시글 삭제", description = "사용자는 본인이 실명으로 작성한 게시물을 삭제한다")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        /**
         * TODO : jwt 확인
         *    권한 처리
         */
        Long accountId = 5234058023853L; //TODO : accountId 어떻게?

        postAppService.removePost(postId, accountId);

        return new ResponseEntity<>(MessageResponse.of(""), HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "postId", description = "댓글 조회할 게시물 pk"),
                    @Parameter(name = "page", description = "페이지 번호"),
                    @Parameter(name = "perPage", description = "페이지 당 댓글 개수")
            }
    )
    @Operation(summary = "게시물에 달린 댓글 조회", description = "게시물에 달린 댓글을 조회한다")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<CommentListResponse> retrievePostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "25") Integer perPage) {

        Long accountId = 5234058023853L; //TODO : jwt에서 추출

        CommentListResponse commentListResponse = commentAppService.retrieveCommentList(
                CommentCommand.CommentListFindCommand.builder()
                        .postId(postId)
                        .page(page)
                        .perPage(perPage)
                        .accountId(accountId)
                        .build()
        );

        return new ResponseEntity<>(commentListResponse, HttpStatus.OK);
    }

}
