package com.seproject.account.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogoutAppService {

//    private static final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    private static final String LOGOUT_URL = "https://kauth.kakao.com/oauth/logout?client_id=";
    private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String REDIRECT_PATH = "&logout_redirect_uri=http://seboard2.site/login";
    private final String KEY = "Authorization";


    public String getRedirectURL() {
        return LOGOUT_URL + clientId + REDIRECT_PATH;
    }

}
