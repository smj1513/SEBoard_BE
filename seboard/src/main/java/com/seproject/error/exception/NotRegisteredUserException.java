package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class NotRegisteredUserException extends AuthenticationException {
    public NotRegisteredUserException(ErrorCode error) {
        super(error.getMessage());
    }
}
