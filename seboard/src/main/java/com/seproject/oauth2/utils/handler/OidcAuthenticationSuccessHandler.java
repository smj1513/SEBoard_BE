package com.seproject.oauth2.utils.handler;

import com.seproject.oauth2.model.Account;
import com.seproject.oauth2.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt;

        if(authentication instanceof OAuth2AuthenticationToken) {
            jwt = jwtProvider.createJWT((OAuth2AuthenticationToken)authentication);
        } else {
            jwt = jwtProvider.createJWT((Account) authentication.getPrincipal());
        }

        String refreshToken = jwtProvider.createRefreshToken();
        response.addHeader("Authorization",jwt);
        response.addHeader("refreshToken",refreshToken);
    }
}
