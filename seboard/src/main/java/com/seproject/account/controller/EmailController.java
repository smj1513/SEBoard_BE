package com.seproject.account.controller;

import com.seproject.account.service.AccountService;
import com.seproject.account.service.email.RegisterEmailService;
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

import java.util.NoSuchElementException;

import static com.seproject.account.controller.dto.EmailDTO.*;


@Tag(name = "이메일 인증 API", description = "이메일 인증(Email) 관련 API")
@AllArgsConstructor
@Controller
public class EmailController {

    private final AccountService accountService;
    private final RegisterEmailService registerEmailService;

    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받을 이메일을 전달한다.")
            }
    )
    @Operation(summary = "이메일 인증 요청", description = "회원가입을 위하여 이메일 인증을 진행한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 이메일 형식"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이미 가입된 아이디가 존재함"),
    })
    @PostMapping("/email/auth")
    public ResponseEntity<?> sendEmail(@RequestBody EmailAuthenticationRequest request) {
        String email = request.getEmail();
        if(!registerEmailService.isEmail(email)){
            return new ResponseEntity<>("잘못된 이메일 형식입니다.", HttpStatus.BAD_REQUEST);
        }
        if(accountService.isExist(email)) {
            return new ResponseEntity<>("이미 인증된 이메일입니다." , HttpStatus.BAD_REQUEST);
        }

        registerEmailService.send(email);
        return new ResponseEntity<>("입력한 이메일로 인증 코드를 전송했습니다.",HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받은 이메일"),
                    @Parameter(name = "authToken", description = "인증번호를 입력한다")
            }
    )
    @Operation(summary = "이메일 인증 확인", description = "회원가입을 진행하기 위해 전송한 인증 코드와 이메일이 유효한지 확인한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "일치하는 정보가 없음"),
    })
    @PostMapping("/email/confirm")
    public ResponseEntity<?> confirmAuthCode(@RequestBody EmailConfirmRequest emailConfirmDTO) {
        try {
            registerEmailService.confirm(emailConfirmDTO.getEmail(), emailConfirmDTO.getAuthToken());
            return new ResponseEntity<>("인증 성공",HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("일치하지 않는 정보",HttpStatus.NOT_FOUND);
        }
    }


    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받을 금오 이메일을 전달한다.")
            }
    )
    @Operation(summary = "금오 이메일 인증 요청", description = "회원가입을 위하여 금오 이메일 인증을 진행한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 이메일 형식")
    })
    @PostMapping("/kumohEmail/auth")
    public ResponseEntity<?> sendKumohEmail(@RequestBody EmailAuthenticationRequest request) {

        String email = request.getEmail();

        if(!registerEmailService.isKumohMail(email)){
            return new ResponseEntity<>("잘못된 이메일 형식입니다.", HttpStatus.BAD_REQUEST);
        }

        registerEmailService.send(email);
        return new ResponseEntity<>("입력한 이메일로 인증 코드를 전송했습니다.",HttpStatus.OK);
    }

    @Parameters(
            {
                    @Parameter(name = "email", description = "인증번호를 받은 금오 이메일"),
                    @Parameter(name = "authToken", description = "인증번호를 입력한다")
            }
    )
    @Operation(summary = "금오 이메일 인증 확인", description = "회원가입을 진행하기 위해 전송한 인증 코드와 이메일이 유효한지 확인한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "404", description = "일치하는 정보가 없음"),
    })
    @PostMapping("/kumohEmail/confirm")
    public ResponseEntity<?> confirmKumohAuthCode(@RequestBody EmailConfirmRequest emailConfirmDTO) {
        try {
            registerEmailService.confirm(emailConfirmDTO.getEmail(), emailConfirmDTO.getAuthToken());
            return new ResponseEntity<>("인증 성공",HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity<>("일치하지 않는 정보",HttpStatus.NOT_FOUND);
        }
    }
}
