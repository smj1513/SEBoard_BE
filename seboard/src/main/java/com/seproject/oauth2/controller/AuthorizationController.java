package com.seproject.oauth2.controller;

import com.seproject.oauth2.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.oauth2.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.seproject.oauth2.controller.dto.AuthorizationDTO.*;

@Tag(name = "접근 권한 관리 API", description = "기능을 사용할 수 있는 권한을 관리하는 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final UrlFilterInvocationSecurityMetaDataSource urlFilterInvocationSecurityMetaDataSource;

    @Operation(summary = "등록된 접근 권한 목록 조회", description = "현재 설정된 접근 권한을 보여준다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllAuthorizationResponse.class)), responseCode = "200", description = "접근 권한 목록 조회 성공"),
    })
    @GetMapping("/authorization")
    public ResponseEntity<?> findAuthorizations() {
        RetrieveAllAuthorizationResponse allAuthorization = authorizationService.findAllAuthorization();
        return new ResponseEntity<>(allAuthorization, HttpStatus.OK);
    }

    @Operation(summary = "접근 권한 추가", description = "접근 권한을 새로 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateAuthorizationResponse.class)), responseCode = "200", description = "추가 성공"),
    })
    @PostMapping("/authorization")
    public ResponseEntity<?> createAuthorization(@RequestBody CreateAuthorizationRequest createAuthorizationRequest) {
        CreateAuthorizationResponse createAuthorizationResponse = authorizationService.addAuthorization(createAuthorizationRequest);
        try{
            urlFilterInvocationSecurityMetaDataSource.reset();
        } catch (Exception e) {
            return new ResponseEntity<>("reload 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(createAuthorizationResponse, HttpStatus.OK);
    }

    @Operation(summary = "접근 권한 삭제", description = "접근 권한을 삭제.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteAuthorizationResponse.class)), responseCode = "200", description = "삭제 성공"),
    })
    @DeleteMapping("/authorization/{authorizationId}")
    public ResponseEntity<?> deleteAuthorization(@PathVariable Long authorizationId) {
        DeleteAuthorizationResponse deleteAuthorizationResponse = authorizationService.deleteAuthorization(authorizationId);
        try{
            urlFilterInvocationSecurityMetaDataSource.reset();
        } catch (Exception e) {
            return new ResponseEntity<>("reload 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(deleteAuthorizationResponse,HttpStatus.OK);
    }
}
