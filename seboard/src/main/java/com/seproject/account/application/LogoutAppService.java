package com.seproject.account.application;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class LogoutAppService {

//    private static final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    private static final String LOGOUT_URL = "https://kauth.kakao.com/oauth/logout?client_id=";
    private static final String POST_METHOD = "POST";
    private static final String ENCRYPTION_PREFIX = "Bearer";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private final String KEY = "Authorization";

    public void logout(String token) {

       try {
           URL url = new URL(LOGOUT_URL + clientId + "&logout_redirect_uri=http://localhost:8080");
//           URL url = new URL("https://kapi.kakao.com/v1/user/unlink");
           HttpURLConnection request = (HttpURLConnection)url.openConnection();
//           request.setRequestMethod(POST_METHOD);
//           request.setRequestProperty(CONTENT_TYPE,X_WWW_FORM_URLENCODED);
//           request.setRequestProperty(KEY,ENCRYPTION_PREFIX + " " + token);

//          request.setRequestMethod(POST_METHOD);
//           request.setRequestProperty(CONTENT_TYPE,X_WWW_FORM_URLENCODED);
//           request.setRequestProperty(KEY,ENCRYPTION_PREFIX + " " + token);
//           System.out.println(request.getResponseCode());
           request.disconnect();
       }
       catch (IOException e) {
            throw new RuntimeException("카카오 서비스와 연결할 수 없습니다.",e);
       }
    }

    public String getRedirectURL() {
        return LOGOUT_URL + clientId + "&logout_redirect_uri=http://localhost:8080";
    }


}

//curl -v -X POST "https://kapi.kakao.com/v1/user/unlink" \
//        -H "Content-Type: application/x-www-form-urlencoded" \
//        -H "Authorization: Bearer ECskf26W1GEj1rCFcynaldKQQJPKLA1NP_oOdm5kCinI2gAAAYfgj-nB"