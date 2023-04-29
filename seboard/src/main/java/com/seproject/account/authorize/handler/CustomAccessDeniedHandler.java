package com.seproject.account.authorize.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

//        if(accessDeniedException instanceof com.seproject.error.exception.AccessDeniedException) {
//            errorCode =
//        } else {
//            response.sendError(HttpStatus.FORBIDDEN.value(),accessDeniedException.getMessage());
//            return;
//        }

        Error error = Error.of(errorCode);
        String result = objectMapper.writeValueAsString(error);
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(result);
    }
}
