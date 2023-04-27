package com.seproject.error.errorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_JWT(HttpStatus.UNAUTHORIZED,100, "유효하지 않은 Json Web Token입니다."),
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
