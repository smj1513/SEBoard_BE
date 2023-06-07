package com.seproject.account.authentication.handler.success;

import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.account.Account;
import com.seproject.account.model.social.KakaoOidcUser;
import com.seproject.account.model.account.OAuthAccount;
import com.seproject.account.model.social.TemporalUserInfo;
import com.seproject.account.model.social.UserToken;
import com.seproject.account.repository.social.OAuthAccountRepository;
import com.seproject.account.repository.social.TemporalUserInfoRepository;
import com.seproject.account.repository.social.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Component
public class OidcAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REDIRECT_URL = "https://seboard.site";

    private final JwtProvider jwtProvider;
    private final OAuthAccountRepository oAuthAccountRepository;
    private final TemporalUserInfoRepository temporalUserInfoRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String jwt;
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        KakaoOidcUser oidcUser = (KakaoOidcUser) oAuth2AuthenticationToken.getPrincipal();

        Optional<OAuthAccount> oAuthAccountBySubAndProvider = oAuthAccountRepository.findOAuthAccountBySubAndProvider(oidcUser.getId(), oidcUser.getProvider());
        if(oAuthAccountBySubAndProvider.isPresent()) {

            OAuthAccount oAuthAccount = oAuthAccountBySubAndProvider.get();
            String id = UUID.randomUUID().toString();
            UserToken userToken = UserToken.builder()
                    .id(id)
                    .account(oAuthAccount)
                    .build();

            userTokenRepository.save(userToken);
            response.sendRedirect(REDIRECT_URL + "/login?id=" + id );
        } else {
            String email = oidcUser.getEmail();

            String id = UUID.randomUUID().toString();

            TemporalUserInfo temporalUserInfo = TemporalUserInfo.builder()
                    .id(id)
                    .subject(oidcUser.getId())
                    .provider(oidcUser.getProvider())
                    .email(email)
                    .name(oidcUser.getName())
                    .nickname(oidcUser.getNickName())
                    .build();

            temporalUserInfoRepository.save(temporalUserInfo);
            response.sendRedirect(REDIRECT_URL + "/signup/oauth?id=" + id);
        }
    }

}

