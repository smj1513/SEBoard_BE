package com.seproject.oauth2.utils.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.oauth2.controller.dto.LoginResponseDTO;
import com.seproject.oauth2.utils.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@Component
public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private JwtProvider jwtProvider;
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
    response, Authentication authentication) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;


        String jwt = jwtProvider.createJWT(token);
        String refreshToken = jwtProvider.createRefreshToken();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();

        String result = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(result);
//        response.setHeader("Authorization",jwt);
//        response.setHeader("refreshToken",refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }
}
