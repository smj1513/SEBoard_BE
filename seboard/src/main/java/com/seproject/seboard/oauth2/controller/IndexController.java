package com.seproject.seboard.oauth2.controller;

import com.seproject.seboard.oauth2.model.PrincipalUser;
import com.seproject.seboard.oauth2.utils.OAuth2Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal PrincipalUser principalUser) {

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
            model.addAttribute("user",userName);
            model.addAttribute("provider",principalUser.getProviderUser().getProvider());
            model.addAttribute("profile",profile);
        }

        return "index";

    }
}
