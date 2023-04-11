package com.seproject.oauth2.controller;

import com.seproject.oauth2.model.ProviderUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<?> index(Authentication authentication, @AuthenticationPrincipal ProviderUser providerUser) {
        return new ResponseEntity<>(providerUser,HttpStatus.OK);

    }
}
