package com.seproject.account.application;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class LogoutAppService {

    private static final String LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    private static final String POST_METHOD = "POST";
    private static final String ENCRYPTION_PREFIX = "Bearer";
    private final String KEY = "Authorization";

    public void logout(String token) {

       try {
           URL url = new URL(LOGOUT_URL);

           HttpURLConnection request = (HttpURLConnection)url.openConnection();
           request.setRequestMethod(POST_METHOD);
           request.setRequestProperty(KEY,ENCRYPTION_PREFIX + " " + token);


           System.out.println("응답코드 : " + request.getResponseCode());
           request.disconnect();
       }
       catch (IOException e) {
            throw new RuntimeException("카카오 서비스와 연결할 수 없습니다.",e);
       }
    }


}
