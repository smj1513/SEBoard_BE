package com.seproject.admin.controller;

import com.seproject.admin.service.RoleService;
import com.seproject.error.Error;
import com.seproject.error.errorCode.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;

import static com.seproject.admin.dto.RoleDTO.*;

@Tag(name = "권한 관리 API", description = "관리자 시스템의 권한 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@RestController
public class AdminRoleController {

    private final RoleService roleService;

    @Operation(summary = "권한 목록 조회", description = "등록된 권한 목록들을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllRoleResponse.class)), responseCode = "200", description = "권한 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 페이징 정보")
    })
    @GetMapping("/roles")
    public ResponseEntity<?> retrieveAllRole(@ModelAttribute RetrieveAllRoleRequest retrieveRoleRequest) {
        int page = Math.max(retrieveRoleRequest.getPage()-1,0);
        int perPage = Math.max(retrieveRoleRequest.getPerPage(),10);

        RetrieveAllRoleResponse roleResponse = roleService.findAll(page, perPage);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @Operation(summary = "권한 생성", description = "새로운 권한을 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateRoleResponse.class)), responseCode = "200", description = "권한 생성 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "400", description = "이미 존재하는 권한 이름")
    })
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody CreateRoleRequest createRoleRequest) {
        CreateRoleResponse createRoleResponse = roleService.createRole(createRoleRequest);
        return new ResponseEntity<>(createRoleResponse, HttpStatus.OK);
    }

    @Operation(summary = "권한 수정", description = "권한 이름이나 설명을 수정한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = UpdateRoleResponse.class)), responseCode = "200", description = "권한 수정 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "400", description = "기본 권한은 수정 불가능"),
    })
    @PutMapping("/roles/{roleId}")
    public ResponseEntity<?> updateRole(@RequestBody UpdateRoleRequest updateRoleRequest,@PathVariable Long roleId) {
        UpdateRoleResponse updateRoleResponse = roleService.updateRole(roleId,updateRoleRequest);
        return new ResponseEntity<>(updateRoleResponse, HttpStatus.OK);
    }

    @Operation(summary = "권한 삭제", description = "권한을 삭제한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateRoleResponse.class)), responseCode = "200", description = "권한 삭제 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "400", description = "기본 권한은 삭제 불가능"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = Error.class)), responseCode = "400", description = "존재하지 않는 권한"),
    })
    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        DeleteRoleResponse deleteRoleResponse = roleService.deleteRole(roleId);
        return new ResponseEntity<>(deleteRoleResponse, HttpStatus.OK);
    }

}
