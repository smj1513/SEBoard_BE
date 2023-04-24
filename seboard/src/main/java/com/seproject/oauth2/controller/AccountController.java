package com.seproject.oauth2.controller;

import com.seproject.oauth2.application.LogoutAppService;
import com.seproject.oauth2.controller.command.AccountRegisterCommand;
import com.seproject.oauth2.controller.command.OAuthAccountCommand;
import com.seproject.oauth2.controller.dto.EmailAuthenticationRequest;
import com.seproject.oauth2.controller.dto.EmailConfirmRequestDTO;
import com.seproject.oauth2.service.AccountService;
import com.seproject.oauth2.service.EmailService;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "계정 시스템 API", description = "계정(Account) 관련 API")
@AllArgsConstructor
@Controller
public class AccountController {

    private final JwtDecoder jwtDecoder;
    private final LogoutAppService logoutAppService;
    private final AccountService accountService;
    private final EmailService emailService;


    @Parameters(
            {
                    @Parameter(name = "Authorization", description = "OAuth 회원가입 버튼을 누르면 전달되는 JWT를 헤더에 넣어서 전달한다."),
                    @Parameter(name = "nickname", description = "oauth 회원가입 추가 정보 중 닉네임"),
                    @Parameter(name = "name", description = "oauth 회원가입 추가 정보 중 닉네임")
            }
    )
    @Operation(summary = "OAuth 회원가입", description = "소셜 로그인 사용시 추가 정보를 입력하여 회원가입을 요청한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "회원가입 성공 시 메세지 전달"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = IllegalArgumentException.class)), responseCode = "400", description = "전달 항목이 비었거나 유효하지 않은경우"),
    })
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

    @Parameters(
            {
                    @Parameter(name = "id", description = "인증한 금오 이메일을 전달한다."),
                    @Parameter(name = "password", description = "계정에 사용될 비밀번호를 전달한다."),
                    @Parameter(name = "nickname", description = "닉네임(활동명)을 입력한다."),
                    @Parameter(name = "name", description = "실명을 입력한다.")
            }
    )
    @Operation(summary = "Form 회원가입", description = "이메일 인증을 완료한 사용자는 회원가입 요청을 진행한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "회원가입 성공 시 메세지 전달"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "인증되지 않은 이메일을 전달"),
    })
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

    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받을 금오 이메일을 전달한다.")
            }
    )
    @Operation(summary = "이메일 인증 요청", description = "회원가입을 위하여 금오 이메일 인증을 진행한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이미 가입된 아이디가 존재함"),
    })
    @PostMapping("/email/auth")
    public ResponseEntity<?> sendEmail(@RequestBody EmailAuthenticationRequest request) {
        String email = request.getEmail();
        if(!email.matches("\\w+@kumoh.ac.kr")){
            return new ResponseEntity<>("잘못된 이메일 형식입니다.",HttpStatus.BAD_REQUEST);
        }
        if(accountService.isExist(email)) {
            return new ResponseEntity<>("이미 인증된 이메일입니다." , HttpStatus.BAD_REQUEST);
        }

        emailService.send(email);
        return new ResponseEntity<>("입력한 이메일로 인증 코드를 전송했습니다.",HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받은 금오 이메일"),
                    @Parameter(name = "authToken", description = "인증번호를 입력한다")
            }
    )
    @Operation(summary = "이메일 인증 확인", description = "회원가입을 진행하기 위해 전송한 인증 코드와 이메일이 유효한지 확인한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "일치하는 정보가 없음"),
    })
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

    @Operation(summary = "로그아웃", description = "미해결 상태")
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
