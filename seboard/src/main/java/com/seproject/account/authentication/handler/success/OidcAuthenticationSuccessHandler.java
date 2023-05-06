package com.seproject.account.authentication.handler.success;

import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.Account;
import com.seproject.account.model.social.KakaoOidcUser;
import com.seproject.account.model.social.OAuthAccount;
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


@RequiredArgsConstructor
@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final TemporalUserInfoRepository temporalUserInfoRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt;
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        KakaoOidcUser oidcUser = (KakaoOidcUser) oAuth2AuthenticationToken.getPrincipal();

        if(oAuthAccountRepository.existsBySubAndProvider(oidcUser.getId(),oidcUser.getProvider())) {
            OAuthAccount oAuthAccount = oAuthAccountRepository.findOAuthAccountBySubAndProvider(oidcUser.getId(),oidcUser.getProvider());
            Account account = oAuthAccount.getAccount();

            String id = UUID.randomUUID().toString();
            UserToken userToken = UserToken.builder()
                    .id(id)
                    .account(account)
                    .build();

            userTokenRepository.save(userToken);
            response.sendRedirect("http://localhost:3000/login?id=" + id );
        } else {
            String email = oidcUser.getEmail();
            jwt = jwtProvider.createTemporalJWT(new UsernamePasswordAuthenticationToken(email,"",oidcUser.getAuthorities()));

            String id = UUID.randomUUID().toString();

            TemporalUserInfo temporalUserInfo = TemporalUserInfo.builder()
                    .id(id)
                    .subject(oidcUser.getId())
                    .provider(oidcUser.getProvider())
                    .email(email)
                    .name(oidcUser.getName())
                    .nickname(oidcUser.getNickName())
                    .accessToken(jwt)
                    .build();

            temporalUserInfoRepository.save(temporalUserInfo);
            response.sendRedirect("http://localhost:3000/signup/oauth?id=" + id);
        }
    }

}

