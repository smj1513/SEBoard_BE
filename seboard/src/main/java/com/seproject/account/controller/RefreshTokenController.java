package com.seproject.account.controller;


import com.seproject.account.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
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
        AccessTokenRefreshResponse responseDTO = tokenService.refresh(refreshToken);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


}
