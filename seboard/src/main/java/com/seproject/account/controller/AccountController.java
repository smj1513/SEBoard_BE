package com.seproject.account.controller;

import com.seproject.account.application.LogoutAppService;
import com.seproject.account.controller.dto.LogoutDTO;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

@Tag(name = "계정 시스템 API", description = "계정(Account) 관련 API")
@AllArgsConstructor
@Controller
public class AccountController {

    private final LogoutAppService logoutAppService;
    private final AccountService accountService;
    private final TokenService tokenService;
    private final JwtDecoder jwtDecoder;

    @Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logoutProc")
    public ResponseEntity<?> logout(Authentication authentication) {

        if(authentication == null) {
            return new ResponseEntity<>("로그인 상태가 아닙니다.",HttpStatus.BAD_REQUEST);
        }

        User user = (User)authentication.getPrincipal();
        String username = user.getUsername();
        String accessToken = jwtDecoder.getAccessToken();

        doLogout(accessToken);

        if(!StringUtils.isEmpty(username) && accountService.isOAuthUser(username)) {
            String redirectURL = logoutAppService.getRedirectURL();
            return new ResponseEntity<>(new LogoutDTO(true,redirectURL), HttpStatus.OK);
        }
        return new ResponseEntity<>(new LogoutDTO(false,""), HttpStatus.OK);

    }

    private void doLogout(String accessToken){
        if (accessToken != null){
            tokenService.deleteAccessToken(accessToken);
        }
    }
}
