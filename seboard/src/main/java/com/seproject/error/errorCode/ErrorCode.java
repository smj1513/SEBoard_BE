package com.seproject.error.errorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_JWT(HttpStatus.UNAUTHORIZED,100, "유효하지 않은 Json Web Token입니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST,101, "비밀번호가 틀렸습니다."),

    INVALID_PAGINATION(HttpStatus.BAD_REQUEST,200, "올바르지 않은 페이지 정보를 전송하였습니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED,102, "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN,103, "접근 권한이 존재하지 않습니다."),
    BANNED_IP(HttpStatus.FORBIDDEN,104, "금지된 ip입니다."),
    NOT_REGISTERED_USER(HttpStatus.TEMPORARY_REDIRECT,105, "추가 정보를 입력하지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED,106, "존재하지 않는 아이디입니다."),
    ;

    ErrorCode(HttpStatus httpStatus, int code,String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
}
