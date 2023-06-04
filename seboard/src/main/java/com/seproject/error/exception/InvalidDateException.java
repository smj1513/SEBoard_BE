package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;

public class InvalidDateException extends BusinessLogicException {
    public InvalidDateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
