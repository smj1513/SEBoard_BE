package com.seproject.error.handler;

import com.seproject.error.Error;
import com.seproject.error.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(TokenValidateException.class)
    public ResponseEntity<Error> handleTokenValidationException(TokenValidateException e) {
        return Error.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<Error> handlePasswordIncorrect(PasswordIncorrectException e) {
        return Error.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<Error> handleRefreshTokenNotFound(RefreshTokenNotFoundException e) {
        return Error.toResponseEntity(e.getErrorCode());
    }

//    @ExceptionHandler(AccessTokenExpiredException.class)
//    public ResponseEntity<Error> handleAccessTokenExpiredException(AccessTokenExpiredException e) {
//        return Error.toResponseEntity(e.getErrorCode());
//    }

    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<Error> handleInvalidPage(InvalidPaginationException e) {
        return Error.toResponseEntity(e.getErrorCode());
    }

}
