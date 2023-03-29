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
@RequestMapping("/auth")
@Tag(name = "익명 리소스에 설정된 비밀번호 검사 API", description = "익명 리소스 권한 관련 API")
@RestController
public class AuthController {


    @Parameters(
            {
                    @Parameter(name = "postId", description = "익명 게시글 pk"),
                    @Parameter(name = "password", description = "사용자가 입력한 비밀번호")
            }
    )
    @Operation(summary = "게시물에 설정된 비밀번호가 일치하는지 검사", description = "익명 게시글에 수정,삭제 권한을 얻기위해 입력한 비밀번호를 검사")
    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> matchPostPassword(@PathVariable Long postId, @RequestBody String password) {

        /**
         * TODO : 패스워드 틀림
         *      존재하지 않는 postId
         */
        return new ResponseEntity<>(password,HttpStatus.OK);
    }


    @Parameters(
            {
                    @Parameter(name = "commentId", description = "익명 댓글,답글 pk"),
                    @Parameter(name = "password", description = "사용자가 입력한 비밀번호")
            }
    )
    @Operation(summary = "익명 댓글, 답글에 설정된 비밀번호가 일치하는지 검사", description = "익명 댓글, 답글의 수정,삭제 권한을 얻기위해 입력한 비밀번호를 검사")
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> matchCommentPassword(@PathVariable Long commentId, @RequestBody String password) {

        /**
         * TODO : 패스워드 틀림
         *      존재하지 않는 commentId
         */
        return new ResponseEntity<>(password,HttpStatus.OK);
    }

}
