package com.seproject.account.token.controller;

import com.seproject.account.account.controller.dto.LoginDTO;
import com.seproject.account.account.domain.OAuthAccount;
import com.seproject.account.social.TemporalUserInfo;
import com.seproject.account.social.repository.TemporalUserInfoRepository;
import com.seproject.account.token.domain.JWT;
import com.seproject.account.token.domain.UserToken;
import com.seproject.account.token.domain.repository.UserTokenRepository;
import com.seproject.account.token.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;


@Tag(name = "토큰 조회 API", description = "로그인 후 토큰 조회 API")
@RequiredArgsConstructor
@RestController
public class UserTokenController {

    private final UserTokenRepository userTokenRepository;
    private final TemporalUserInfoRepository temporalUserInfoRepository;
    private final TokenService tokenService;
    @GetMapping("/auth/kakao")
    public ResponseEntity<?> findUserToken(@RequestParam String id) {
        Optional<UserToken> userTokenOptional = userTokenRepository.findById(id);

        if(userTokenOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserToken userToken = userTokenOptional.get();
        userTokenRepository.delete(userToken);

        OAuthAccount account = userToken.getAccount();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(account.getSub(), UUID.randomUUID().toString(),account.getAuthorities());

        JWT accessToken = tokenService.createAccessToken(token);
        JWT refreshToken = tokenService.createLargeRefreshToken(token);

        LoginDTO.LoginResponseDTO loginResponseDTO = LoginDTO.LoginResponseDTO.builder()
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build();

        return new ResponseEntity<>(loginResponseDTO,HttpStatus.OK);
    }

    @GetMapping("/register/oauth")
    public ResponseEntity<?> findTemporalUserInfo(@RequestParam("id") String id) {
        Optional<TemporalUserInfo> optionalTemporalUserInfo = temporalUserInfoRepository.findById(id);

        if(optionalTemporalUserInfo.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TemporalUserInfo temporalUserInfo = optionalTemporalUserInfo.get();
        temporalUserInfoRepository.delete(temporalUserInfo);

        return new ResponseEntity<>(temporalUserInfo,HttpStatus.OK);
    }


}
