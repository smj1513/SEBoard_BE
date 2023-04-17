package com.seproject.oauth2.utils.handler;

import com.seproject.oauth2.model.Account;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
    response, Authentication authentication) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        Account account = (Account)token.getPrincipal();

        String jwt = jwtProvider.createJWT(account);
        String refreshToken = jwtProvider.createRefreshToken();

        response.setHeader("Authorization",jwt);
        response.setHeader("refreshToken",refreshToken);
        response.setStatus(HttpStatus.OK.value());
    }
}
