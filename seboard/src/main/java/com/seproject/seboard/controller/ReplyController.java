package com.seproject.seboard.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.comment.ReplyRequest.*;

@AllArgsConstructor
@RequestMapping("/reply")
@RestController
public class ReplyController {

    @PostMapping("/unnamed")
    public ResponseEntity<?> createUnnamedReply(@RequestBody CreateUnnamedReplyRequest request) {

        /**
         * TODO : tagAuthor가 존재하지 않음
         *          content가 비어있음
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/unnamed/{replyId}")
    public ResponseEntity<?> updateUnnamedReply(@RequestBody UpdateUnnamedReplyRequest request) {

        /**
         * TODO : 비밀번호가 다른 경우
         *      contents가 비어있음
         *      존재하지 않는 replyId
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }


    @DeleteMapping("/unnamed/{replyId}")
    public ResponseEntity<?> deleteUnnamedReply(@PathVariable Long replyId, @RequestBody String password) {

        /**
         * TODO : 존재하지 않는 답글
         *          비밀번호가 틀림
         */

        return new ResponseEntity<>(password,HttpStatus.OK);
    }


    @PostMapping("/named")
    public ResponseEntity<?> createNamedReply(@RequestBody CreateNamedReplyRequest request) {

        /**
         * TODO : jwt
         *        답글 작성 성공
         *        존재하지 않는 commentId
         *        존재하지 않는 tag
         *        본문이 비어있음
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/named/{replyId}")
    public ResponseEntity<?> updateNamedReply(@RequestBody String contents) {

        /**
         * TODO :
         *      jwt
         *      권한이 없는 경우
         *      contents가 비어있음
         *      존재하지 않는 replyId
         */

        return new ResponseEntity<>(contents,HttpStatus.OK);
    }


    @DeleteMapping("/named/{replyId}")
    public ResponseEntity<?> deleteNamedReply(@PathVariable Long replyId) {

        /**
         * TODO : jwt
         *        존재하지 않는 답글
         *        권한이 없음
         */

        return new ResponseEntity<>(replyId,HttpStatus.OK);
    }

}
