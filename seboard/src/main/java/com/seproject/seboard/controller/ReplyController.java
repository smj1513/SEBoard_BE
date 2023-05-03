package com.seproject.seboard.controller;

import com.seproject.seboard.application.CommentAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.comment.ReplyRequest.CreateReplyRequest;
import com.seproject.seboard.controller.dto.comment.ReplyRequest.UpdateReplyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "답글 API", description = "답글(reply) 관련 API")
@AllArgsConstructor
@RequestMapping("/reply")
@RestController
public class ReplyController {
    private final CommentAppService commentAppService;

    @Parameter(name = "request", description = "답글을 달 댓글의 pk, 태그할 댓글(또는 답글)의 pk, 태그 작성자 정보, 본문, 익명 작성자 정보를 전달")
    @Operation(summary = "답글 작성", description = "답글을 작성한다")
    @PostMapping()
    public ResponseEntity<?> createReply(@RequestBody CreateReplyRequest request) {
        Long accountId = 5234058023853L;

        /**
         * TODO : tagAuthor가 존재하지 않음
         *          content가 비어있음
         */
        commentAppService.writeReply(request.toCommand(accountId));

        return new ResponseEntity<>(MessageResponse.of(""), HttpStatus.CREATED);
    }

    @Parameters(
            {
                    @Parameter(name = "replyId", description = "수정할 댓글의 pk"),
                    @Parameter(name = "request", description = "수정된 본문, 댓글의 비밀번호 정보")
            }
    )
    @Operation(summary = "답글 수정", description = "답글을 수정한다")
    @PutMapping("/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable Long replyId , @RequestBody UpdateReplyRequest request) {
        Long accountId = 5234058023853L; //TODO : jwt

        /**
         * TODO : 비밀번호가 다른 경우
         *      contents가 비어있음
         *      존재하지 않는 replyId
         */
        commentAppService.editReply(request.toCommand(replyId, accountId));

        return new ResponseEntity<>(MessageResponse.of("") ,HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "replyId", description = "삭제할 게시글 pk"),
            }
    )
    @Operation(summary = "답글 삭제", description = "답글을 삭제한다")
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId) {
        Long accountId = 5234058023853L; //TODO : jwt

        /**
         * TODO : 존재하지 않는 답글
         *          비밀번호가 틀림
         */
        commentAppService.removeReply(replyId, accountId);

        return new ResponseEntity<>(MessageResponse.of(""),HttpStatus.OK);
    }
}
