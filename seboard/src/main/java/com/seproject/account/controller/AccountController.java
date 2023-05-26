package com.seproject.account.controller;

import com.seproject.account.jwt.JWT;
import com.seproject.account.model.account.Account;
import com.seproject.account.service.LogoutService;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.email.KumohEmailService;
import com.seproject.account.service.email.PasswordChangeEmailService;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.CustomAuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.seproject.account.controller.dto.AccountDTO.*;
import static com.seproject.account.controller.dto.LogoutDTO.*;

@Tag(name = "계정 시스템 API", description = "계정(Account) 관련 API")
@AllArgsConstructor
@Controller
public class AccountController {

    private final LogoutService logoutService;
    private final AccountService accountService;
    private final JwtDecoder jwtDecoder;
    private final PasswordChangeEmailService passwordChangeEmailService;
    private final KumohEmailService kumohEmailService;

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logoutProc")
    public ResponseEntity<?> logout( @RequestBody LogoutRequestDTO request) {

        Account account = SecurityUtils.getAccount().orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN,null));

        String accessToken = jwtDecoder.getAccessToken();
        String refreshToken = request.getRefreshToken();
        JWT jwt = new JWT(accessToken,refreshToken);

        logoutService.logout(jwt);

        String principal = account.getUsername();
        if(!StringUtils.isEmpty(principal) && accountService.isOAuthUser(principal)) {
            String redirectURL = logoutService.getRedirectURL();
            return new ResponseEntity<>(new LogoutResponseDTO(true,redirectURL), HttpStatus.OK);
        }

        return new ResponseEntity<>(new LogoutResponseDTO(false,""), HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 찾기", description = "아이디를 이용하여 비밀번호 변경")
    @PostMapping("/password")
    public ResponseEntity<?> findPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        if(!passwordChangeEmailService.isConfirmed(resetPasswordRequest.getEmail())) {
            return Error.toResponseEntity(ErrorCode.EMAIL_NOT_FOUNT);
        }

        return new ResponseEntity<>(accountService.resetPassword(resetPasswordRequest),HttpStatus.OK);
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

    @Operation(summary = "내 정보 조회", description = "마이페이지에 필요한 정보 조회")
    @GetMapping("/mypage")
    public ResponseEntity<?> findUserInfo() {

        String loginId = SecurityUtils.getLoginId();

        if(loginId == null) return Error.toResponseEntity(ErrorCode.NOT_LOGIN);

        return new ResponseEntity<>(accountService.findMyInfo(loginId),HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경", description = "마이페이지에서 비밀번호를 변경함")
    @PostMapping("/mypage/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        accountService.changePassword(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 시도")
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody  WithDrawAccountRequest request) {
        Account account = SecurityUtils.getAccount().orElseThrow(() -> new CustomAuthenticationException(ErrorCode.NOT_LOGIN,null));

        String accessToken = jwtDecoder.getAccessToken();
        String refreshToken = request.getRefreshToken();
        JWT jwt = new JWT(accessToken,refreshToken);

        logoutService.logout(jwt);

        String principal = account.getUsername();


        if(!StringUtils.isEmpty(principal) && accountService.isOAuthUser(principal)) {
            String redirectURL = logoutService.getRedirectURL();
            accountService.deleteAccount(account.getAccountId());
            return new ResponseEntity<>(WithDrawAccountResponse.toDTO(account,true,redirectURL), HttpStatus.OK);
        }
        accountService.deleteAccount(account.getAccountId());
        return new ResponseEntity<>(WithDrawAccountResponse.toDTO(account,false,null),HttpStatus.OK);
    }

}
