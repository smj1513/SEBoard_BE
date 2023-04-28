package com.seproject.oauth2.utils.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorCode errorCode = ErrorCode.NOT_LOGIN;

//        if(exception instanceof NotLoginedException) {
//            errorCode
//        } else {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(),exception.getMessage());
//            return;
//        }

        Error error = Error.of(errorCode);
        String result = objectMapper.writeValueAsString(error);
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(result);
    }
}
