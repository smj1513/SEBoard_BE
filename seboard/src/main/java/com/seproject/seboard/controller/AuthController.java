package com.seproject.seboard.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public class AuthController {


    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> matchPostPassword(@PathVariable Long postId, @RequestBody String password) {

        /**
         * TODO : 패스워드 틀림
         *      존재하지 않는 postId
         */
        return new ResponseEntity<>(password,HttpStatus.OK);
    }


    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> matchCommentPassword(@PathVariable Long commentId, @RequestBody String password) {

        /**
         * TODO : 패스워드 틀림
         *      존재하지 않는 commentId
         */
        return new ResponseEntity<>(password,HttpStatus.OK);
    }

}
