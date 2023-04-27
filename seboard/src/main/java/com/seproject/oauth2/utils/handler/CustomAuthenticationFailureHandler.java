package com.seproject.oauth2.utils.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.PasswordIncorrectException;
import com.seproject.error.exception.TokenValidateException;
import com.seproject.error.handler.AuthenticationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorCode errorCode;

        if(exception instanceof PasswordIncorrectException) {
            errorCode = ErrorCode.PASSWORD_INCORRECT;
        } else if(exception instanceof TokenValidateException){
            errorCode = ErrorCode.INVALID_JWT;
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),exception.getMessage());
            return;
        }

        Error error = Error.of(errorCode);
        String result = objectMapper.writeValueAsString(error);
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(result);
    }

}
