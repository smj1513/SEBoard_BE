package com.seproject.oauth2.utils.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.oauth2.controller.dto.LoginResponseDTO;
import com.seproject.oauth2.model.social.KakaoOidcUser;
import com.seproject.oauth2.repository.AccountRepository;
import com.seproject.oauth2.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt;
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
        KakaoOidcUser oidcUser = (KakaoOidcUser) token.getPrincipal();

        if(accountRepository.existsByLoginId("kakao_"+oidcUser.getId())) {
            jwt = jwtProvider.createJWT(new UsernamePasswordAuthenticationToken(oidcUser.getName(), UUID.randomUUID(),oidcUser.getAuthorities()));
        } else {
            jwt = jwtProvider.createJWT(token);
        }

        String refreshToken = jwtProvider.createRefreshToken();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();

        String result = objectMapper.writeValueAsString(responseDTO);
        response.getWriter().write(result);
//        response.addHeader("Authorization",jwt);
//        response.addHeader("refreshToken",refreshToken);
    }
}
