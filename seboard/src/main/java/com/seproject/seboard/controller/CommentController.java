package com.seproject.seboard.controller;

import com.seproject.seboard.controller.dto.comment.CommentRequest;
import com.seproject.seboard.controller.dto.comment.CommentRequest.CreateNamedCommentRequest;
import com.seproject.seboard.controller.dto.comment.CommentRequest.CreateUnnamedCommentRequest;
import com.seproject.seboard.controller.dto.comment.CommentRequest.UpdateUnnamedCommentRequest;
import com.seproject.seboard.controller.dto.user.AnonymousRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Tag(name = "댓글 API", description = "댓글(comments) 관련 API")
@RequestMapping("/comments")
@RestController
public class CommentController {

    @Parameter(name = "request", description = "댓글 달려고 하는 게시글의 pk, 댓글 내용을 전달")
    @Operation(summary = "Named 댓글 작성", description = "사용자는 실명으로 댓글을 작성한다.")
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


    @Parameters(
            {
                    @Parameter(name = "commentId", description = "수정할 댓글의 pk"),
                    @Parameter(name = "contents", description = "수정할 내용")
            }
    )
    @Operation(summary = "Named 댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정한다.")
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
    @Parameter(name = "commentId", description = "삭제할 댓글의 pk")
    @Operation(summary = "Named 댓글 삭제", description = "사용자는 자신이 작성한 댓글을 삭제한다")
    @DeleteMapping("/named/{commentId}")
    public ResponseEntity<?> deleteNamedComment(@PathVariable Long commentId) {

        /**
         * TODO : jwt
         *  댓글 수정 권한이 없을때
         *  존재하지 않는 commentId
         */

        return new ResponseEntity<>(commentId, HttpStatus.OK);
    }

    @Parameter(name = "request", description = "댓글이 달릴 게시물의 pk, 댓글 내용, 작성자 정보를 전달")
    @Operation(summary = "익명 댓글 작성", description = "사용자는 익명으로 댓글을 작성한다")
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

    @Parameters(
            {
                    @Parameter(name = "commentId", description = "수정할 댓글의 pk"),
                    @Parameter(name = "request", description = "수정할 내용, 작성자 정보를 가진다")
            }
    )
    @Operation(summary = "익명 댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정한다.")
    @PutMapping("/unnamed/{commentId}")
    public ResponseEntity<?> updateUnnamedComment(@PathVariable Long commentId,@RequestBody UpdateUnnamedCommentRequest request) {

        /**
         * TODO : 존재하지 않는 commentId
         *  contens가 비어있음
         *  비밀번호가 틀림
         */

        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "commentId", description = "삭제할 댓글의 pk"),
                    @Parameter(name = "password", description = "댓글에 설정된 비밀번호")
            }
    )
    @Operation(summary = "익명 댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정한다.")
    @DeleteMapping("/unnamed/{commentId}")
    public ResponseEntity<?> deleteUnnamedComment(@PathVariable Long commentId, @RequestBody String password) {

        /**
         * TODO : 존재하지 않는 commentId
         *  비밀번호가 틀림
         */

        return new ResponseEntity<>(password, HttpStatus.OK);
    }

}
