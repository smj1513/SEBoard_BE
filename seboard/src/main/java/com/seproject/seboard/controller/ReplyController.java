package com.seproject.seboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.dto.comment.ReplyRequest.*;

@Tag(name = "답글 API", description = "답글(reply) 관련 API")
@AllArgsConstructor
@RequestMapping("/reply")
@RestController
public class ReplyController {

    @Parameter(name = "request", description = "답글을 달 댓글의 pk, 태그할 댓글(또는 답글)의 pk, 태그 작성자 정보, 본문, 익명 작성자 정보를 전달")
    @Operation(summary = "익명 답글 작성", description = "답글을 익명으로 작성한다")
    @PostMapping("/unnamed")
    public ResponseEntity<?> createUnnamedReply(@RequestBody CreateUnnamedReplyRequest request) {

        /**
         * TODO : tagAuthor가 존재하지 않음
         *          content가 비어있음
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "replyId", description = "수정할 댓글의 pk"),
                    @Parameter(name = "request", description = "수정된 본문, 댓글의 비밀번호 정보")
            }
    )
    @Operation(summary = "익명 답글 수정", description = "비밀번호 입력으로 익명 답글을 수정한다")
    @PutMapping("/unnamed/{replyId}")
    public ResponseEntity<?> updateUnnamedReply(@PathVariable Long replyId , @RequestBody UpdateUnnamedReplyRequest request) {

        /**
         * TODO : 비밀번호가 다른 경우
         *      contents가 비어있음
         *      존재하지 않는 replyId
         */

        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "replyId", description = "삭제할 게시글 pk"),
                    @Parameter(name = "password", description = "삭제할 답글의 비밀번호 입력")
            }
    )
    @Operation(summary = "익명 답글 삭제", description = "비밀번호 입력으로 익명 답글을 삭제한다")
    @DeleteMapping("/unnamed/{replyId}")
    public ResponseEntity<?> deleteUnnamedReply(@PathVariable Long replyId, @RequestBody String password) {

        /**
         * TODO : 존재하지 않는 답글
         *          비밀번호가 틀림
         */

        return new ResponseEntity<>(password,HttpStatus.OK);
    }

    @Parameter(name = "request", description = "상위 댓글의 pk, 태그할 댓글(또는 답글)의 pk, 태그된 작성자의 정보, 답글 내용 정보를 전달")
    @Operation(summary = "Named 답글 작성", description = "사용자는 실명으로 답글을 작성한다.")
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


    @Parameters(
            {
                    @Parameter(name = "replyId", description = "수정할 답글의 pk"),
                    @Parameter(name = "contents", description = "수정할 답글의 내용")
            }
    )
    @Operation(summary = "Named 답글 수정", description = "사용자는 자신이 작성한 답글을 수정한다.")
    @PutMapping("/named/{replyId}")
    public ResponseEntity<?> updateNamedReply(@PathVariable Long replyId, @RequestBody String contents) {

        /**
         * TODO :
         *      jwt
         *      권한이 없는 경우
         *      contents가 비어있음
         *      존재하지 않는 replyId
         */

        return new ResponseEntity<>(contents,HttpStatus.OK);
    }

    @Parameter(name = "replyId", description = "삭제할 답글의 pk")
    @Operation(summary = "Named 답글 삭제", description = "사용자는 자신이 작성한 답글을 삭제한다.")
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
