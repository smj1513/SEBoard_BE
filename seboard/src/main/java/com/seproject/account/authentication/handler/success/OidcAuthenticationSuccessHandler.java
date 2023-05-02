package com.seproject.account.authentication.handler.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.account.model.social.KakaoOidcUser;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.service.TokenService;
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

import static com.seproject.account.controller.dto.LoginDTO.*;

@RequiredArgsConstructor
@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
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

        String refreshToken = jwtProvider.createRefreshToken(token);

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

        tokenService.addToken(jwt,refreshToken,oidcUser.getAuthorities());

    }
}
