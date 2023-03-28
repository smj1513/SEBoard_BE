package com.seproject.seboard.controller;

import com.seproject.seboard.dto.user.AnonymousRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.comment.CommentRequest.*;

@AllArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

    @PostMapping("/named")
    public ResponseEntity<?> createNamedComment(@RequestBody CreateNamedCommentRequest request) {

        Long postId = request.getPostId();
        String contents = request.getContents();


        /**
         * TODO : jwt
         *  required 체크
         *  존재하지 않는 postId
         *  contens가 비어있음
         */


        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/named/{commentId}")
    public ResponseEntity<?> updateNamedComment(@PathVariable Long commentId,@RequestBody String contents) {

        /**
         * TODO : jwt
         *  댓글 수정 권한이 없을때
         *  존재하지 않는 commentId
         *  contens가 비어있음
         */

        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    @DeleteMapping("/named/{commentId}")
    public ResponseEntity<?> deleteNamedComment(@PathVariable Long commentId) {

        /**
         * TODO : jwt
         *  댓글 수정 권한이 없을때
         *  존재하지 않는 commentId
         */

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @PostMapping("/unnamed")
    public ResponseEntity<?> createUnnamedComment(@RequestBody CreateUnnamedCommentRequest request) {

        Long postId = request.getPostId();
        String contents = request.getContents();
        AnonymousRequest author = request.getAuthor();

        /**
         * TODO : 존재하지 않는 postId
         *  contens가 비어있음
         */


        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/unnamed/{commentId}")
    public ResponseEntity<?> updateUnnamedComment(@PathVariable Long commentId,@RequestBody UpdateUnnamedCommentRequest request) {

        /**
         * TODO : 존재하지 않는 commentId
         *  contens가 비어있음
         *  비밀번호가 틀림
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @DeleteMapping("/unnamed/{commentId}")
    public ResponseEntity<?> deleteUnnamedComment(@PathVariable Long commentId, @RequestBody String password) {

        /**
         * TODO : 존재하지 않는 commentId
         *  비밀번호가 틀림
         */

        return new ResponseEntity<>(password, HttpStatus.OK);
    }

}
