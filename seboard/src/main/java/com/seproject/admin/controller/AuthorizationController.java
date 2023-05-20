package com.seproject.admin.controller;

import com.seproject.account.authorize.url.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.account.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.seproject.admin.dto.AuthorizationDTO.*;

@Tag(name = "접근 권한 관리 API", description = "기능을 사용할 수 있는 권한을 관리하는 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final UrlFilterInvocationSecurityMetaDataSource urlFilterInvocationSecurityMetaDataSource;

    @Operation(summary = "카테고리에 설정된 권한 조회", description = "카테고리에 설정된 접근 권한을 보여준다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryAuthorizationRetrieveResponses.class)), responseCode = "200", description = "접근 권한 목록 조회 성공"),
    })
    @GetMapping("/authorization/category/{categoryId}")
    public ResponseEntity<?> retrieveByCategoryId(@PathVariable Long categoryId) {
        List<CategoryAuthorization> byCategory = authorizationService.findByCategory(categoryId);
        return new ResponseEntity<>(CategoryAuthorizationRetrieveResponses.toDTO(byCategory), HttpStatus.OK);
    }

    @Operation(summary = "카테고리에 게시글 작성(생성, 수정, 삭제) 권한 삭제", description = "카테고리에 글 작성 권한을 삭제한다.")
    @Parameters(value = {
            @Parameter(name = "categoyId" , schema = @Schema(implementation = Long.class)),
            @Parameter(name = "requestBody" , schema = @Schema(implementation = CategoryAccessUpdateRequest.class),
                    description = "접근 권한(access), 글 작성 권한(write), 메뉴 노출 대상(menu), 게시판 관리 권한(manager)\n" +
                            "roles : [roleId, roleId, roleId]")
    })
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryAuthorizationRetrieveResponses.class)), responseCode = "200", description = "권한 수정가 성공"),
    })
    @PostMapping("/authorization/category/{categoryId}")
    public ResponseEntity<?> updateCategoryAccess(@PathVariable long categoryId,@RequestBody CategoryAccessUpdateRequest request) {

        authorizationService.update(categoryId,request);

        try{
            urlFilterInvocationSecurityMetaDataSource.reset();
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<CategoryAuthorization> byCategory = authorizationService.findByCategory(categoryId);
        return new ResponseEntity<>(CategoryAuthorizationRetrieveResponses.toDTO(byCategory),HttpStatus.OK);
    }

}
