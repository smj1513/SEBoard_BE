package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;

import java.util.NoSuchElementException;

public class RefreshTokenNotFoundException extends NoSuchElementException {

    public RefreshTokenNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
