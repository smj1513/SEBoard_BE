package com.seproject.account.authentication.handler.success;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.account.model.Account;
import com.seproject.account.model.social.KakaoOidcUser;
import com.seproject.account.model.social.OAuthAccount;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.social.TemporalUserInfo;
import com.seproject.account.model.social.UserToken;
import com.seproject.account.repository.OAuthAccountRepository;
import com.seproject.account.repository.TemporalUserInfoRepository;
import com.seproject.account.repository.UserTokenRepository;
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
    private final OAuthAccountRepository oAuthAccountRepository;
    private final TokenService tokenService;
    private final TemporalUserInfoRepository temporalUserInfoRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt;
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
        KakaoOidcUser oidcUser = (KakaoOidcUser) token.getPrincipal();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if(oAuthAccountRepository.existsBySubAndProvider(oidcUser.getId(),oidcUser.getProvider())) {
            OAuthAccount oAuthAccount = oAuthAccountRepository.findOAuthAccountBySubAndProvider(oidcUser.getId(),oidcUser.getProvider());
            Account account = oAuthAccount.getAccount();
            jwt = jwtProvider.createJWT(new UsernamePasswordAuthenticationToken(oAuthAccount.getSub(),"",account.getAuthorities()));
            String refreshToken = jwtProvider.createRefreshToken(token);

            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken)
                    .build();
            String id = UUID.randomUUID().toString();
            UserToken userToken = UserToken.builder()
                    .id(id)
                    .userToken(responseDTO)
                    .build();

            userTokenRepository.save(userToken);
            tokenService.addToken(jwt,refreshToken,oidcUser.getAuthorities());

            response.sendRedirect("http://localhost:3000/login?id=" + id );
        } else {
            String sub = oidcUser.getId();
            jwt = jwtProvider.createTemporalJWT(new UsernamePasswordAuthenticationToken(sub,"",oidcUser.getAuthorities()));

            TemporalLoginResponseDTO responseDTO = TemporalLoginResponseDTO.builder()
                    .subject(sub)
                    .provider(oidcUser.getProvider())
                    .email(oidcUser.getEmail())
                    .name(oidcUser.getName())
                    .nickname(oidcUser.getNickName())
                    .accessToken(jwt)
                    .build();

            String id = UUID.randomUUID().toString();
            TemporalUserInfo temporalUserInfo = TemporalUserInfo.builder()
                    .id(id)
                    .userInfo(responseDTO)
                    .build();

            temporalUserInfoRepository.save(temporalUserInfo);
            response.sendRedirect("http://localhost:3000/signup/oauth?id=" + id);
        }


    }
}

