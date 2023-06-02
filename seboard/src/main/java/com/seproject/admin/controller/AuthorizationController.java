package com.seproject.admin.controller;

import com.seproject.account.authorize.url.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.account.service.AuthorizationService;

import com.seproject.admin.domain.AccessOption;
import com.seproject.admin.domain.MenuAuthorization;
import com.seproject.admin.service.AdminMenuService;
import com.seproject.seboard.application.CategoryAppService;
import com.seproject.seboard.application.dto.category.CategoryCommand;
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
import java.util.Map;

import static com.seproject.admin.dto.AuthorizationDTO.*;

@Tag(name = "접근 권한 관리 API", description = "기능을 사용할 수 있는 권한을 관리하는 API")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AuthorizationController {

    private final AdminMenuService adminMenuService;
    private final CategoryAppService categoryAppService;
    private final UrlFilterInvocationSecurityMetaDataSource urlFilterInvocationSecurityMetaDataSource;

    @Operation(summary = "카테고리에 설정된 권한 조회", description = "카테고리에 설정된 접근 권한을 보여준다.")
    @ApiResponses({
    })
    @GetMapping("/authorization/category/{categoryId}")
    public ResponseEntity<?> retrieveByCategoryId(@PathVariable Long categoryId) {
        CategoryAccessOptionResponse retrieve = adminMenuService.retrieve(categoryId);

        return new ResponseEntity<>(retrieve,HttpStatus.OK);
    }

    @Operation(summary = "카테고리에 게시글 작성(생성, 수정, 삭제) 권한 삭제", description = "카테고리에 글 작성 권한을 삭제한다.")
    @Parameters(value = {
            @Parameter(name = "categoyId" , schema = @Schema(implementation = Long.class)),
            @Parameter(name = "requestBody" , schema = @Schema(implementation = CategoryAccessUpdateRequest.class),
                    description = "접근 권한(access), 글 작성 권한(write), 메뉴 노출 대상(menu), 게시판 관리 권한(manager)\n" +
                            "roles : [roleId, roleId, roleId]")
    })
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "권한 수정가 성공"),
    })
    @PostMapping("/authorization/category/{categoryId}")
    public ResponseEntity<?> updateCategoryAccess(@PathVariable long categoryId,@RequestBody CategoryAccessUpdateRequest request) {
        adminMenuService.update(categoryId,request);
        categoryAppService.updateCategory(
                new CategoryCommand.CategoryUpdateCommand(categoryId,
                        request.getName(),
                        request.getUrlId(),
                        request.getExternalUrl())
        );

        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
