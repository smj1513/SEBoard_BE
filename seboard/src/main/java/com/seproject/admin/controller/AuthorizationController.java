package com.seproject.admin.controller;

import com.seproject.account.authorize.url.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.account.model.role.RoleAuthorization;
import com.seproject.account.model.role.auth.CategoryAuthorization;
import com.seproject.account.service.AuthorizationService;
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

    @Operation(summary = "카테고리에 게시글 접근(조회) 권한 추가", description = "카테고리에 접근 권한을 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "권한 추가 성공"),
    })
    @PostMapping("/authorization/category/read")
    public ResponseEntity<?> addToCategoryAuthorization(@RequestBody AddRoleToCategoryAuthorizationRequest request) {
        Long categoryId = request.getCategoryId();
        Long roleId = request.getRoleId();
        RoleAuthorization roleAuthorization = authorizationService.addRoleToCategoryReadable(roleId, categoryId);
        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
            return new ResponseEntity<>(AddRoleToCategoryAuthorizationResponse.toDTO(roleAuthorization),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Operation(summary = "카테고리 접근 권한 추가", description = "카테고리에 접근 권한을 새로 추가한다.")
//    @ApiResponses({
//            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateAuthorizationResponse.class)), responseCode = "200", description = "추가 성공"),
//    })
//    @PostMapping("/authorization")
//    public ResponseEntity<?> createAuthorization(@RequestBody CreateAuthorizationRequest createAuthorizationRequest) {
//        long menuId = createAuthorizationRequest.getMenuId();
//        long roleId = createAuthorizationRequest.getRoleId();
//        //        authorizationService.addAuthorization(menuId,roleId);
//        return null;
//    }

//    @Operation(summary = "접근 권한 삭제", description = "접근 권한을 삭제.")
//    @ApiResponses({
//            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteAuthorizationResponse.class)), responseCode = "200", description = "삭제 성공"),
//    })
//    @DeleteMapping("/authorization/{authorizationId}")
//    public ResponseEntity<?> deleteAuthorization(@PathVariable Long authorizationId) {
//        DeleteAuthorizationResponse deleteAuthorizationResponse = authorizationService.deleteAuthorization(authorizationId);
//        try{
//            //TODO : 옵저버
//            urlFilterInvocationSecurityMetaDataSource.reset();
//        } catch (Exception e) {
//            return new ResponseEntity<>("reload 실패하였습니다.", HttpStatus.SERVICE_UNAVAILABLE);
//        }
//        return new ResponseEntity<>(deleteAuthorizationResponse,HttpStatus.OK);
//    }
}
