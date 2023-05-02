package com.seproject.account.controller;

import com.seproject.account.controller.command.AccountRegisterCommand;
import com.seproject.account.controller.command.OAuthAccountCommand;
import com.seproject.account.controller.dto.RegisterDTO;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.EmailService;
import com.seproject.account.jwt.JwtDecoder;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

import static com.seproject.account.controller.dto.RegisterDTO.*;

@Tag(name = "회원가입 API", description = "회원가입(Register) 관련 API")
@AllArgsConstructor
@Controller
public class RegisterController {

    private final JwtDecoder jwtDecoder;
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
    public ResponseEntity<?> registerUserWithOAuth(HttpServletRequest request, @RequestBody OAuth2RegisterRequest oAuth2RegisterRequest) {

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
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
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

    @Operation(summary = "닉네임 중복 체크", description = "이미 존재하는 닉네임인지 중복 확인을 한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = ConfirmDuplicateNicknameResponse.class)), responseCode = "200", description = "닉네임 중복 여부 응답"),
    })
    @PostMapping("/nickname")
    public ResponseEntity<?> confirmDuplicateNickname(@RequestBody ConfirmDuplicateNicknameRequest confirmDuplicateNicknameRequest) {


        String nickname = confirmDuplicateNicknameRequest.getNickname();
        boolean existNickname = accountService.isExistByNickname(nickname);

        return new ResponseEntity<>(ConfirmDuplicateNicknameResponse.toDTO(existNickname),HttpStatus.OK);
    }
}
