package com.seproject.oauth2.controller;

import com.seproject.oauth2.model.PrincipalUser;
import com.seproject.oauth2.utils.OAuth2Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<?> index(Authentication authentication, @AuthenticationPrincipal PrincipalUser principalUser) {

        if(authentication != null) {
            String userName;
            String profile;
            if(authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
                profile = principalUser.getProviderUser().getPicture();
                userName = OAuth2Utils.oAuth2UserName(token, principalUser);
            } else {
                profile = "none";
                userName = principalUser.getProviderUser().getUsername();
            }

        }
        log.info("{}",authentication);
        return new ResponseEntity<>(principalUser.getProviderUser(),HttpStatus.OK);

    }
}
