package com.seproject.account.controller;

import com.seproject.account.application.LogoutAppService;
import com.seproject.account.controller.dto.LogoutDTO;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.email.KumohEmailService;
import com.seproject.account.service.email.PasswordChangeEmailService;
import com.seproject.account.service.TokenService;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.seproject.account.controller.dto.AccountDTO.*;

@Tag(name = "계정 시스템 API", description = "계정(Account) 관련 API")
@AllArgsConstructor
@Controller
public class AccountController {

    private final LogoutAppService logoutAppService;
    private final AccountService accountService;
    private final TokenService tokenService;
    private final JwtDecoder jwtDecoder;
    private final PasswordChangeEmailService passwordChangeEmailService;
    private final KumohEmailService kumohEmailService;

    @Operation(summary = "로그아웃", description = "로그아웃")
    @GetMapping("/logoutProc")
    public ResponseEntity<?> logout(Authentication authentication) {

        if(authentication == null) {
            return new ResponseEntity<>("로그인 상태가 아닙니다.",HttpStatus.BAD_REQUEST);
        }

        User user = (User)authentication.getPrincipal();
        String principal = user.getUsername();
        String accessToken = jwtDecoder.getAccessToken();

        doLogout(accessToken);

        if(!StringUtils.isEmpty(principal) && accountService.isOAuthUser(principal)) {
            String redirectURL = logoutAppService.getRedirectURL();
            return new ResponseEntity<>(new LogoutDTO(true,redirectURL), HttpStatus.OK);
        }
        return new ResponseEntity<>(new LogoutDTO(false,""), HttpStatus.OK);

    }

    @Operation(summary = "비밀번호 찾기", description = "아이디를 이용하여 비밀번호 변경")
    @PostMapping("/password")
    public ResponseEntity<?> findLoginId(@RequestBody PasswordRequest passwordRequest) {

        if(!passwordChangeEmailService.isConfirmed(passwordRequest.getEmail())) {
            return Error.toResponseEntity(ErrorCode.EMAIL_NOT_FOUNT);
        }

        return new ResponseEntity<>(accountService.changePassword(passwordRequest),HttpStatus.OK);
    }

    @Operation(summary = "금오인 인증", description = "금오 이메일 인증 후 전환")
    @PostMapping("/kumoh")
    public ResponseEntity<?> findLoginId(@RequestBody KumohAuthRequest kumohAuthRequest) {

        String email = kumohAuthRequest.getEmail();

        if(!kumohEmailService.isConfirmed(email)) {
            return Error.toResponseEntity(ErrorCode.EMAIL_NOT_FOUNT);
        }

        return new ResponseEntity<>(accountService.grantKumohAuth(kumohAuthRequest),HttpStatus.OK);
    }

    private void doLogout(String accessToken){
        if (accessToken != null){
            tokenService.deleteAccessToken(accessToken);
        }
    }



}
