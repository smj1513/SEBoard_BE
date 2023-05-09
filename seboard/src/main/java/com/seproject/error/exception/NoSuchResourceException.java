package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;

public class NoSuchResourceException extends AbstractErrorException implements BusinessLogicException{

    public NoSuchResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
