package com.seproject.account.controller;

import com.seproject.account.jwt.JWT;
import com.seproject.account.model.Account;
import com.seproject.account.model.social.OAuthAccount;
import com.seproject.account.model.social.TemporalUserInfo;
import com.seproject.account.model.social.UserToken;
import com.seproject.account.repository.social.TemporalUserInfoRepository;
import com.seproject.account.repository.social.UserTokenRepository;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.email.RegisterEmailService;
import com.seproject.account.service.TokenService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.seproject.account.controller.dto.LoginDTO.*;

import java.util.Optional;

import static com.seproject.account.controller.dto.RegisterDTO.*;

@Tag(name = "회원가입 API", description = "회원가입(Register) 관련 API")
@AllArgsConstructor
@Controller
public class RegisterController {

    private final AccountService accountService;
    private final RegisterEmailService registerEmailService;
    private final TemporalUserInfoRepository temporalUserInfoRepository;
    private final UserTokenRepository userTokenRepository;
    private final TokenService tokenService;

    @Operation(summary = "OAuth 회원가입", description = "소셜 로그인 사용시 추가 정보를 입력하여 회원가입을 요청한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "회원가입 성공 시 메세지 전달"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = IllegalArgumentException.class)), responseCode = "400", description = "전달 항목이 비었거나 유효하지 않은경우"),
    })
    @PostMapping("/account/oauth")
    public ResponseEntity<?> registerUserWithOAuth(@RequestBody OAuth2RegisterRequest oAuth2RegisterRequest) {

        String email = oAuth2RegisterRequest.getEmail();

        boolean confirmed = registerEmailService.isConfirmed(email);
        boolean isDuplicateUser = accountService.isExistByNickname(oAuth2RegisterRequest.getNickname());

        if(!confirmed) return new ResponseEntity<>("인증되지 않은 이메일입니다.",HttpStatus.BAD_REQUEST);
        if(isDuplicateUser) return new ResponseEntity<>("중복된 닉네임을 가진 사용자입니다.",HttpStatus.BAD_REQUEST);

        try{
            OAuthAccount oAuthAccount = accountService.register(oAuth2RegisterRequest);

            Account account = oAuthAccount.getAccount();
            JWT jwt  = tokenService.createToken(account);

            String accessToken = jwt.getAccessToken();
            String refreshToken = jwt.getRefreshToken();

            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }

    }

    @Parameters(
            {
                    @Parameter(name = "id", description = "인증한 이메일을 전달한다."),
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

        boolean confirmed = registerEmailService.isConfirmed(id);
        boolean isDuplicateUser = accountService.isExistByNickname(formRegisterRequest.getNickname());

        if(!confirmed) return new ResponseEntity<>("인증되지 않은 이메일입니다.",HttpStatus.BAD_REQUEST);
        if(isDuplicateUser) return new ResponseEntity<>("중복된 닉네임을 가진 사용자입니다.",HttpStatus.BAD_REQUEST);

        Account account = accountService.register(formRegisterRequest);
        JWT token = tokenService.createToken(account);

        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();


        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복 체크", description = "이미 존재하는 닉네임인지 중복 확인을 한다")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = ConfirmDuplicateNicknameResponse.class)), responseCode = "200", description = "닉네임 중복 여부 응답"),
    })
    @PostMapping("account/nickname")
    public ResponseEntity<?> confirmDuplicateNickname(@RequestBody ConfirmDuplicateNicknameRequest confirmDuplicateNicknameRequest) {
        String nickname = confirmDuplicateNicknameRequest.getNickname();
        boolean existNickname = accountService.isExistByNickname(nickname);

        return new ResponseEntity<>(ConfirmDuplicateNicknameResponse.toDTO(existNickname),HttpStatus.OK);
    }


    @GetMapping("/auth/kakao")
    public ResponseEntity<?> findUserToken(@RequestParam String id) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findById(id);

        if(userTokenOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserToken userToken = userTokenOptional.get();
        userTokenRepository.delete(userToken);

        Account account = userToken.getAccount();
        JWT token = tokenService.createToken(account);

        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();

        return new ResponseEntity<>(loginResponseDTO,HttpStatus.OK);
    }

    @GetMapping("/register/oauth")
    public ResponseEntity<?> findTemporalUserInfo(@RequestParam("id") String id) {
        Optional<TemporalUserInfo> optionalTemporalUserInfo = temporalUserInfoRepository.findById(id);

        if(optionalTemporalUserInfo.isEmpty()) {
            return new ResponseEntity<>("존재하지 않는 데이터",HttpStatus.NOT_FOUND);
        }

        TemporalUserInfo temporalUserInfo = optionalTemporalUserInfo.get();
        temporalUserInfoRepository.delete(temporalUserInfo);

        return new ResponseEntity<>(temporalUserInfo,HttpStatus.OK);
    }

}
