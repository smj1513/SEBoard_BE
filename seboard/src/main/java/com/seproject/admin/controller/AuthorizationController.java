package com.seproject.admin.controller;

import com.seproject.account.authorize.url.UrlFilterInvocationSecurityMetaDataSource;
import com.seproject.account.model.role.Role;
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
            @ApiResponse(content = @Content(schema = @Schema(implementation = AddRoleToCategoryResponse.class)), responseCode = "200", description = "권한 추가 성공"),
    })
    @PostMapping("/authorization/category/read")
    public ResponseEntity<?> addReadabilityToCategory(@RequestBody AddRoleToCategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Long roleId = request.getRoleId();
        Role role = authorizationService.addReadabilityToCategory(roleId, categoryId);
        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
            return new ResponseEntity<>(AddRoleToCategoryResponse.toDTO(role),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "카테고리에 게시글 접근(조회) 권한 삭제", description = "카테고리에 접근 권한을 삭제한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "200", description = "권한 삭제 성공"),
    })
    @DeleteMapping("/authorization/category/read")
    public ResponseEntity<?> deleteReadabilityToCategory(@RequestBody DeleteRoleToCategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Long roleId = request.getRoleId();
        Role role = authorizationService.deleteReadabilityToCategory(roleId, categoryId);
        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
            return new ResponseEntity<>(DeleteRoleToCategoryResponse.toDTO(role),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "카테고리에 게시글 글 작성(생성, 수정, 삭제) 권한 추가", description = "카테고리에 글 작성 권한을 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = AddRoleToCategoryResponse.class)), responseCode = "200", description = "권한 추가 성공"),
    })
    @PostMapping("/authorization/category/write")
    public ResponseEntity<?> addWritabilityToCategory(@RequestBody AddRoleToCategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Long roleId = request.getRoleId();
        Role role = authorizationService.addWritabilityToCategory(roleId, categoryId);
        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
            return new ResponseEntity<>(AddRoleToCategoryResponse.toDTO(role),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "카테고리에 게시글 작성(생성, 수정, 삭제) 권한 삭제", description = "카테고리에 글 작성 권한을 삭제한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DeleteRoleToCategoryResponse.class)), responseCode = "200", description = "권한 삭제 성공"),
    })
    @DeleteMapping("/authorization/category/write")
    public ResponseEntity<?> deleteWritabilityToCategory(@RequestBody DeleteRoleToCategoryRequest request) {
        Long categoryId = request.getCategoryId();
        Long roleId = request.getRoleId();
        Role role = authorizationService.deleteWritabilityToCategory(roleId, categoryId);
        try {
            urlFilterInvocationSecurityMetaDataSource.reset();
            return new ResponseEntity<>(DeleteRoleToCategoryResponse.toDTO(role),HttpStatus.OK);
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
