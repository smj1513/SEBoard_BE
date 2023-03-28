package com.seproject.seboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("bookmark/posts/{postId}/")
@Tag(name = "북마크 API", description = "게시물 즐겨찾기 관련 API")
public class BookmarkController {

    @Operation(summary = "게시글 즐겨찾기 등록",
            description = "사용자가 게시글을 북마크 등록 요청")
    @PostMapping
    public ResponseEntity<?> createBookmark(@PathVariable Long postId){
        /**
         * TODO : 존재하지 않는 postId
         *          jwt
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

    @Operation(summary = "게시글 즐겨찾기 해제",
            description = "사용자가 게시글을 북마크 해제 요청")
    @DeleteMapping
    public ResponseEntity<?> cancelBookmark(@PathVariable Long postId){
        /**
         * TODO : 존재하지 않는 postId
         *          jwt
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }
}
