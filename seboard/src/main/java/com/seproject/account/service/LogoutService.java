package com.seproject.account.service;


import com.seproject.account.jwt.JWT;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.model.token.LogoutLargeRefreshToken;
import com.seproject.account.model.token.LogoutRefreshToken;
import com.seproject.account.model.token.LogoutToken;
import com.seproject.account.repository.token.LogoutLargeRefreshTokenRepository;
import com.seproject.account.repository.token.LogoutRefreshTokenRepository;
import com.seproject.account.repository.token.LogoutTokenRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService {

    private static final String LOGOUT_URL = "https://kauth.kakao.com/oauth/logout?client_id=";
    private static final String REDIRECT_PATH = "&logout_redirect_uri=http://seboard2.site/login";
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final LogoutTokenRepository logoutTokenRepository;
    private final LogoutRefreshTokenRepository logoutRefreshTokenRepository;
    private final LogoutLargeRefreshTokenRepository logoutLargeRefreshTokenRepository;
    private final JwtDecoder jwtDecoder;

    public String getRedirectURL() {
        return LOGOUT_URL + clientId + REDIRECT_PATH;
    }

    public void logout(JWT jwt) { //TODO : largeRefreshToken
        String accessToken = jwt.getAccessToken();
        String refreshToken = jwt.getRefreshToken();
        logoutTokenRepository.save(new LogoutToken(accessToken));

        try{
            if(jwtDecoder.isLargeToken(refreshToken)) {
                logoutLargeRefreshTokenRepository.save(new LogoutLargeRefreshToken(refreshToken));
            } else {
                logoutRefreshTokenRepository.save(new LogoutRefreshToken(refreshToken));
            }
        } catch (CustomAuthenticationException e) {
            throw new CustomAuthenticationException(ErrorCode.DISABLE_REFRESH_TOKEN,null);
        }

    }

}
