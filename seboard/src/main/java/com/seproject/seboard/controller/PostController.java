package com.seproject.seboard.controller;

import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.PaginationResponse;
import com.seproject.seboard.controller.dto.post.PostRequest;
import com.seproject.seboard.controller.dto.post.PostRequest.CreateNamedPostRequest;
import com.seproject.seboard.controller.dto.post.PostRequest.CreateUnnamedPostRequest;
import com.seproject.seboard.controller.dto.post.PostRequest.UpdateNamedPostRequest;
import com.seproject.seboard.controller.dto.post.PostResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponse;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostListResponseElement;
import com.seproject.seboard.controller.dto.post.PostResponse.RetrievePostResponse;
import com.seproject.seboard.controller.dto.user.AnonymousRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Tag(name = "게시글 API", description = "게시글(posts) 관련 API")
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    @Parameters(
            {
                    @Parameter(name = "categoryId", description = "조회하고자 하는 카테고리 pk"),
                    @Parameter(name = "page", description = "페이지 번호"),
                    @Parameter(name = "perPage", description = "페이지 당 게시글 개수")
            }
    )
    @Operation(summary = "게시글 목록 조회", description = "카테고리, 페이징 정보를 전달하여 게시글 목록 조회한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostListResponseElement.class)),responseCode = "200" , description = "조회 성공"),
    })
    @GetMapping
    public ResponseEntity<?> retrievePostList(
            @RequestParam Long categoryId,
            @RequestParam Integer page,
            @RequestParam Integer perPage
    ) {

        List<RetrievePostListResponseElement> elements = List.of();
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .currentPage(page)
                .contentSize(20)
                .perPage(10)
                .lastPage(10)
                .build();
        RetrievePostListResponse retrievePostListResponse = RetrievePostListResponse.toDTO(elements, paginationResponse);
        /***
         *  TODO:  페이지 번호 0일때
         *          페이지 번호 MAX 이상
         *          category id가 없는 경우, 설정 안된경우
         */
        return new ResponseEntity<>(retrievePostListResponse,HttpStatus.OK);
    }

    @Parameter(name = "postId", description = "상세 조회를 할 게시물의 pk")
    @Operation(summary = "게시글 상세 조회", description = "게시글을 클릭하면 게시글의 상세 내역을 조회한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrievePostResponse.class)),responseCode = "200" , description = "조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = MessageResponse.class)),responseCode = "404" , description = "해당 pk를 가진 게시물이 없음")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> retrievePost(@PathVariable("postId") Long postId){

        /***
         * TODO : jwt 추가
         *      없는 postId를 조회
         */

        try {
            RetrievePostResponse retrievePostResponse = null;
            return new ResponseEntity<>(retrievePostResponse,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            MessageResponse response = MessageResponse.toDTO(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @Parameter(name = "postId", description = "즐겨찾기 지정할 게시물의 pk")
    @Operation(summary = "게시글 북마크 지정", description = "사용자가 게시글을 즐겨찾기로 등록한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = MessageResponse.class)),responseCode = "200" , description = "북마크 성공"),
    })
    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<?> createBookmark(@PathVariable Long postId) {

        // JWT에서 사용자 정보 추출

        /**
         * TODO : 존재하지 않는 postId
         *  jwt가 없거나 유효하지 않는 경우
         */
        return new ResponseEntity<>(MessageResponse.toDTO("북마크 등록 성공"),HttpStatus.OK);
    }

    @Parameter(name = "postId", description = "즐겨찾기 해제할 게시물의 pk")
    @Operation(summary = "게시글 북마크 해제", description = "사용자가 즐겨찾기한 게시물을 즐겨찾기 해제한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = MessageResponse.class)),responseCode = "200" , description = "북마크 해제 성공"),
    })
    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long postId) {

        // JWT에서 사용자 정보 추출

        /**
         * TODO : 존재하지 않는 postId
         *  jwt가 없거나 유효하지 않는 경우
         */
        return new ResponseEntity<>(MessageResponse.toDTO("북마크 해제 성공"),HttpStatus.OK);
    }

    @Parameter(name = "request", description = "게시물 생성에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 공지사항 여부 정보")
    @Operation(summary = "Named 게시글 작성", description = "사용자는 실명으로 게시글을 작성한다")
    @PostMapping("/named")
    public ResponseEntity<?> createNamedPost(@RequestBody CreateNamedPostRequest request) { //TODO : 첨부파일 필드 추가
         String title = request.getTitle();
         String contents = request.getContents();
         List<MultipartFile> attachment = request.getAttachment();
         Long categoryId = request.getCategoryId();
         boolean pined = request.isPined();

        /**
         * TODO : required 필드 null 체크
         *    공지 작성 권한 처리
         *    존재하지 않는 categoryId
         *    제목 또는 본문이 비어있거나 길이 초과
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }


    @Parameters(
        {
            @Parameter(name = "postId", description = "수정할 게시글 pk"),
            @Parameter(name = "request", description = "게시물 수정에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 공지사항 여부 정보")
        }
    )
    @Operation(summary = "Named 게시글 수정", description = "사용자는 본인이 실명으로 작성한 게시물을 수정한다")
    @PutMapping("/named/{postId}")
    public ResponseEntity<?> updateNamedPost(@PathVariable Long postId,@RequestBody UpdateNamedPostRequest request) { //TODO : 첨부파일 필드 추가
        String title = request.getTitle();
        String contents = request.getContents();
        List<MultipartFile> attachment = request.getAttachment();
        Long categoryId = request.getCategoryId();
        boolean pined = request.isPined();

        /**
         * TODO : required 필드 null 체크
         *    권한 처리
         *    존재하지 않는 categoryId
         *    제목 또는 본문이 비어있거나 길이 초과
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @Parameter(name = "postId", description = "삭제할 게시글의 pk")
    @Operation(summary = "Named 게시글 삭제", description = "사용자는 본인이 실명으로 작성한 게시물을 삭제한다")
    @DeleteMapping("/named/{postId}")
    public ResponseEntity<?> deleteNamedPost(@PathVariable Long postId) {
        /**
         * TODO : jwt 확인
         *    권한 처리
         */

        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

    @Parameter(name = "request", description = "익명 게시물 생성에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 익명 작성자 정보")
    @Operation(summary = "Unnamed 게시글 작성", description = "사용자는 익명으로 게시물을 작성한다.")
    @PostMapping("/unnamed")
    public ResponseEntity<?> createUnnamedPost(@RequestBody CreateUnnamedPostRequest request) { //TODO : 첨부파일 필드 추가
        String title = request.getTitle();
        String contents = request.getContents();
        List<MultipartFile> attachment = request.getAttachment();
        Long categoryId = request.getCategoryId();
        AnonymousRequest author = request.getAuthor();

        /**
         * TODO : required 필드 확인
         *    존재하지 않는 categoryId
         *    제목 또는 본문이 비어있거나 길이 초과
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @Parameters(
        {
            @Parameter(name = "postId", description = "수정할 게시물 pk"),
            @Parameter(name = "request", description = "익명 게시물 수정에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 익명 작성자 정보")
        }
    )
    @Operation(summary = "Unnamed 게시글 수정", description = "사용자가 익명으로 작성한 게시물을 수정한다")
    @PutMapping("/unnamed/{postId}")
    public ResponseEntity<?> updateUnnamedPost(@PathVariable Long postId,@RequestBody CreateUnnamedPostRequest request) { //TODO : 첨부파일 필드 추가

        String title = request.getTitle();
        String contents = request.getContents();
        List<MultipartFile> attachment = request.getAttachment();
        Long categoryId = request.getCategoryId();
        AnonymousRequest author = request.getAuthor();

        /**
         * TODO : required 필드 확인
         *      존재하지 않는 익명 게시글
         *      비밀번호가 다름
         *      존재하지 않는 postId
         *      존재하지 않는 categoryId
         *      제목 또는 본문이 비어있거나 길이 초과
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "postId", description = "삭제할 게시물 pk"),
                    @Parameter(name = "password", description = "익명 게시물 수정에 필요한 제목, 본문 , 첨부파일, 카테고리 pk, 익명 작성자 정보")
            }
    )
    @Operation(summary = "Unnamed 게시글 삭제", description = "사용자가 익명으로 작성한 게시물을 삭제한다")
    @DeleteMapping("/unnamed/{postId}")
    public ResponseEntity<?> deleteUnnamedPost(@PathVariable Long postId,@RequestBody String password) {

        /**
         * TODO : 비밀번호 틀림
         *    존재하지 않는 게시글
         */

        return new ResponseEntity<>(password,HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "postId", description = "댓글 조회할 게시물 pk"),
                    @Parameter(name = "pageNum", description = "페이지 번호"),
                    @Parameter(name = "perPage", description = "페이지 당 댓글 개수")
            }
    )
    @Operation(summary = "게시물에 달린 댓글 조회", description = "게시물에 달린 댓글을 조회한다")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> retrievePostComments(
            @PathVariable Long postId,
            @RequestParam Integer pageNum,
            @RequestParam Integer perPage) {


        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

}
