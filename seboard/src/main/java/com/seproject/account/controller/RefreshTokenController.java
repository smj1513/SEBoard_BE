package com.seproject.account.controller;

import com.seproject.account.controller.dto.TokenDTO;
import com.seproject.account.jwt.JwtDecoder;
import com.seproject.account.jwt.JwtProvider;
import com.seproject.account.model.AccessToken;
import com.seproject.account.model.RefreshToken;
import com.seproject.account.repository.AccountRepository;
import com.seproject.account.repository.RefreshTokenRepository;
import com.seproject.account.service.AccountService;
import com.seproject.account.service.CustomUserDetailsService;
import com.seproject.account.service.TokenService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.RefreshTokenNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;

import static com.seproject.account.controller.dto.TokenDTO.*;

@Tag(name = "토큰 재발행 시스템 API", description = "토큰 재발행 API")
@AllArgsConstructor
@Controller
public class RefreshTokenController {

    private final TokenService tokenService;




    @Operation(summary = "토큰 재발행", description = "refreshToken을 이용한 토큰 재 발행")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = AccessTokenRefreshResponse.class)), responseCode = "200", description = "토큰 재발행 성공")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshUserToken(@RequestBody AccessTokenRefreshRequest accessTokenRefreshRequest) {
        String refreshToken = accessTokenRefreshRequest.getRefreshToken();
        RefreshToken findRefreshToken = tokenService.findRefreshToken(refreshToken);

        if(findRefreshToken == null) throw new RefreshTokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);

        AccessTokenRefreshResponse responseDTO = tokenService.refresh(refreshToken);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}
