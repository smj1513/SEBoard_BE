package com.seproject.oauth2.controller;

import com.seproject.oauth2.application.LogoutAppService;
import com.seproject.oauth2.controller.command.AccountRegisterCommand;
import com.seproject.oauth2.controller.command.OAuthAccountCommand;
import com.seproject.oauth2.controller.dto.EmailAuthenticationRequest;
import com.seproject.oauth2.controller.dto.EmailConfirmRequestDTO;
import com.seproject.oauth2.service.AccountService;
import com.seproject.oauth2.service.EmailService;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

import java.util.NoSuchElementException;

import static com.seproject.oauth2.controller.dto.RegisterDTO.*;

@AllArgsConstructor
@Controller
public class AccountController {

    private final JwtDecoder jwtDecoder;
    private final LogoutAppService logoutAppService;
    private final AccountService accountService;
    private final EmailService emailService;

    @PostMapping("/account/oauth")
    public ResponseEntity<?> registerUserWithOAuth(HttpServletRequest request,@RequestBody OAuth2RegisterRequest oAuth2RegisterRequest) {

        String token = request.getHeader("Authorization");

        if(token == null) throw new IllegalArgumentException("인증 정보가 없음");

        try{
            OAuthAccountCommand accountCommand = OAuthAccountCommand.builder()
                    .id(jwtDecoder.getLoginId(token))
                    .provider(jwtDecoder.getProvider(token))
                    .email(jwtDecoder.getEmail(token))
                    .profile(jwtDecoder.getProfile(token))
                    .nickname(oAuth2RegisterRequest.getNickname())
                    .name(oAuth2RegisterRequest.getName())
                    .build();
            accountService.register(accountCommand);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("회원가입 완료",HttpStatus.OK);
    }

    @PostMapping("/account/form")
    public ResponseEntity<?> registerUserWithForm(@RequestBody FormRegisterRequest formRegisterRequest) {

        String id = formRegisterRequest.getId();

        boolean confirmed = emailService.isConfirmed(id);

        if(confirmed) {
            accountService.register(AccountRegisterCommand.builder()
                    .id(id)
                    .password(formRegisterRequest.getPassword())
                    .name(formRegisterRequest.getName())
                    .nickname(formRegisterRequest.getNickname()).build());
            return new ResponseEntity<>("가입 완료",HttpStatus.OK);
        }

        return new ResponseEntity<>("인증되지 않은 이메일입니다.",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/email/auth")
    public ResponseEntity<?> sendEmail(@RequestBody EmailAuthenticationRequest request) {

        if(accountService.isExist(request.getEmail())) {
            return new ResponseEntity<>("이미 인증된 이메일입니다." , HttpStatus.BAD_REQUEST);
        }

        emailService.send(request.getEmail());
        return new ResponseEntity<>("입력한 이메일로 인증 코드를 전송했습니다.",HttpStatus.OK);
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<?> confirmAuthCode(@RequestBody EmailConfirmRequestDTO emailConfirmDTO) {
        try {
            emailService.confirm(emailConfirmDTO.getEmail(), emailConfirmDTO.getAuthToken());
            return new ResponseEntity<>("인증 성공",HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("일치하지 않는 정보",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/logoutProc") //미해결
    public ResponseEntity<?> logout(Authentication authentication) {
        System.out.println("----호출----");
        if(authentication == null) {
            return new ResponseEntity<>("로그인 상태가 아닙니다.",HttpStatus.BAD_REQUEST);
        }

        if(authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
            logoutAppService.logout(token.getAuthorizedClientRegistrationId());

            return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
        }

        return null;
    }
}
