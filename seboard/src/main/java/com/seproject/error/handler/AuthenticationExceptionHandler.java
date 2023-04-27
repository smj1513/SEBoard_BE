package com.seproject.error.handler;

import com.seproject.error.Error;
import com.seproject.error.exception.TokenValidateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(TokenValidateException.class)
    public ResponseEntity<Error> handleTokenValidationException(TokenValidateException e) {
        return Error.toResponseEntity(e.getErrorCode());
    }

}
