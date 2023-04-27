package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidPaginationException extends RuntimeException {
    private ErrorCode errorCode;

    public InvalidPaginationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
