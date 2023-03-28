package com.seproject.seboard.controller;

import com.seproject.seboard.domain.model.post.Category;
import com.seproject.seboard.dto.post.PostResponse;
import com.seproject.seboard.dto.user.UserRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.seproject.seboard.dto.post.PostRequest.*;
import static com.seproject.seboard.dto.post.PostResponse.*;

@Slf4j
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    @GetMapping// 게시판 목록 조회
    public ResponseEntity<?> retrievePostList(@ModelAttribute RetrievePostListRequest request) {
        Long categoryId = request.getCategoryId();
        Integer page = request.getPage();
        Integer perPage = request.getPerPage();
        RetrievePostListResponse retrievePostListResponse = RetrievePostListResponse.toDTO(null, null);
        /***
         *  TODO:  페이지 번호 0일때
         *          페이지 번호 MAX 이상
         *          category id가 없는 경우, 설정 안된경우
         */
        return new ResponseEntity<>(retrievePostListResponse,HttpStatus.OK);
    }

    @GetMapping("/{postId}") // 게시글 상세 조회
    public ResponseEntity<RetrievePostResponse> retrievePost(@PathVariable("postId") Long postId){

        /***
         * TODO : jwt 추가
         *      없는 postId를 조회
         */

        RetrievePostResponse retrievePostResponse = null;

        return new ResponseEntity<>(retrievePostResponse,HttpStatus.OK);
    }

    @PostMapping("/{postId}/bookmark") // 북마크 등록
    public ResponseEntity<?> createBookmark(@PathVariable Long postId) {

        // JWT에서 사용자 정보 추출

        /**
         * TODO : 존재하지 않는 postId
         *  jwt가 없거나 유효하지 않는 경우
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long postId) {

        // JWT에서 사용자 정보 추출

        /**
         * TODO : 존재하지 않는 postId
         *  jwt가 없거나 유효하지 않는 경우
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

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


    @PutMapping("/named/{postId}")
    public ResponseEntity<?> updateNamedPost(@RequestBody UpdateNamedPostRequest request) { //TODO : 첨부파일 필드 추가
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

    @DeleteMapping("/named/{postId}")
    public ResponseEntity<?> deleteNamedPost(@PathVariable Long postId) {
        /**
         * TODO : jwt 확인
         *    권한 처리
         */

        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

    @PostMapping("/unnamed")
    public ResponseEntity<?> createUnnamedPost(@RequestBody CreateUnnamedPostRequest request) { //TODO : 첨부파일 필드 추가
        String title = request.getTitle();
        String contents = request.getContents();
        List<MultipartFile> attachment = request.getAttachment();
        Long categoryId = request.getCategoryId();
        UserRequest author = request.getAuthor();

        /**
         * TODO : required 필드 확인
         *    존재하지 않는 categoryId
         *    제목 또는 본문이 비어있거나 길이 초과
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @PutMapping("/unnamed/{postId}")
    public ResponseEntity<?> updateUnnamedPost(@PathVariable Long postId,@RequestBody CreateUnnamedPostRequest request) { //TODO : 첨부파일 필드 추가

        String title = request.getTitle();
        String contents = request.getContents();
        List<MultipartFile> attachment = request.getAttachment();
        Long categoryId = request.getCategoryId();
        UserRequest author = request.getAuthor();

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

    @DeleteMapping("/unnamed/{postId}")
    public ResponseEntity<?> deleteUnnamedPost(@PathVariable Long postId,@RequestBody String password) {

        /**
         * TODO : 비밀번호 틀림
         *    존재하지 않는 게시글
         */

        return new ResponseEntity<>(password,HttpStatus.OK);
    }

//    @GetMapping("/{:postId}/comments")
//    public ResponseEntity<?> retrievePostComments(@PathVariable Long postId,)

}
