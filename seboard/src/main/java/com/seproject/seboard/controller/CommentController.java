package com.seproject.seboard.controller;

import com.seproject.seboard.application.CommentAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.comment.CommentRequest.CreateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.seboard.controller.dto.comment.CommentRequest.*;

@AllArgsConstructor
@Tag(name = "댓글 API", description = "댓글(comments) 관련 API")
@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentAppService commentAppService;

    @Parameter(name = "request", description = "댓글 달려고 하는 게시글의 pk, 댓글 내용을 전달")
    @Operation(summary = "댓글 작성", description = "사용자는 실명으로 댓글을 작성한다.")
    @PostMapping()
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        Long accountId = 5234058023853L; //TODO : jwt

        /**
         * TODO : jwt
         *  required 체크
         *  존재하지 않는 postId
         *  contens가 비어있음
         */

        commentAppService.writeComment(request.toCommand(accountId));


        return new ResponseEntity<>(request, HttpStatus.OK);
    }


    @Parameters(
            {
                    @Parameter(name = "commentId", description = "수정할 댓글의 pk"),
                    @Parameter(name = "contents", description = "수정할 내용")
            }
    )
    @Operation(summary = "댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정한다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request) {
        Long accountId = 5234058023853L; //TODO : jwt
        /**
         * TODO : jwt
         *  댓글 수정 권한이 없을때
         *  존재하지 않는 commentId
         *  contens가 비어있음
         */
        commentAppService.editComment(
                request.toCommand(commentId, accountId)
        );

        return new ResponseEntity<>(MessageResponse.of(""), HttpStatus.OK);
    }

    @Parameter(name = "commentId", description = "삭제할 댓글의 pk")
    @Operation(summary = "댓글 삭제", description = "사용자는 자신이 작성한 댓글을 삭제한다")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteNamedComment(@PathVariable Long commentId) {
        Long accountId = 5234058023853L;

        /**
         * TODO : jwt
         *  댓글 수정 권한이 없을때
         *  존재하지 않는 commentId
         */
        commentAppService.removeComment(commentId, accountId);

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }
}
