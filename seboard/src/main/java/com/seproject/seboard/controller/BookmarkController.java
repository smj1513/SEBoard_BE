package com.seproject.seboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @Parameter(name = "postId", description = "즐겨찾기 지정할 게시물의 pk")
    @Operation(summary = "게시글 북마크 지정", description = "사용자가 게시글을 즐겨찾기로 등록한다")
    @PostMapping
    public ResponseEntity<?> createBookmark(@PathVariable Long postId){
        /**
         * TODO : 존재하지 않는 postId
         *          jwt
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }

    @Parameter(name = "postId", description = "즐겨찾기 해제할 게시물의 pk")
    @Operation(summary = "게시글 북마크 해제", description = "사용자가 즐겨찾기한 게시물을 즐겨찾기 해제한다")
    @DeleteMapping
    public ResponseEntity<?> cancelBookmark(@PathVariable Long postId){
        /**
         * TODO : 존재하지 않는 postId
         *          jwt
         */
        return new ResponseEntity<>(postId,HttpStatus.OK);
    }
}
