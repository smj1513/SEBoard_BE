package com.seproject.error.exception;

import com.seproject.error.errorCode.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AccessTokenExpiredException extends AuthenticationException {

    private final ErrorCode errorCode;
    public AccessTokenExpiredException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = ErrorCode.ACCESS_TOKEN_EXPIRED;
    }
}
