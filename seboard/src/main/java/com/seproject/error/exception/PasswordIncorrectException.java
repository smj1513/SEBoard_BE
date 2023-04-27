package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

@Getter
public class PasswordIncorrectException extends BadCredentialsException {

    private ErrorCode errorCode;
    public PasswordIncorrectException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public PasswordIncorrectException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
    }
}
