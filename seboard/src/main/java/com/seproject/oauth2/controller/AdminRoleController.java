package com.seproject.oauth2.controller;

import com.seproject.oauth2.controller.dto.AccountDTO;
import com.seproject.oauth2.service.RoleService;
import com.seproject.oauth2.utils.jwt.JwtDecoder;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.seproject.oauth2.controller.dto.RoleDTO.*;

@Tag(name = "권한 관리 API", description = "관리자 시스템의 권한 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin")
@Controller
public class AdminRoleController {

    private final JwtDecoder jwtDecoder;
    private final RoleService roleService;

    @Operation(summary = "권한 목록 조회", description = "등록된 권한 목록들을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = RetrieveAllRoleResponse.class)), responseCode = "200", description = "권한 목록 조회 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "잘못된 페이징 정보")
    })
//    @JWT
    @GetMapping("/roles")
    public ResponseEntity<?> retrieveAllRole(HttpServletRequest request, @RequestBody RetrieveAllRoleRequest retrieveRoleRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

        int page = retrieveRoleRequest.getPage();
        int perPage = retrieveRoleRequest.getPerPage();
        page = Math.max(page-1,0);
        perPage = Math.max(perPage,1);

        try{
            RetrieveAllRoleResponse roleResponse = roleService.findAll(page, perPage);
            return new ResponseEntity<>(roleResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("페이지 번호가 잘못되었습니다.",HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "권한 생성", description = "새로운 권한을 추가한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = CreateRoleResponse.class)), responseCode = "200", description = "권한 생성 성공"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = String.class)), responseCode = "400", description = "이미 존재하는 권한 이름")
    })
//    @JWT
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(HttpServletRequest request, @RequestBody CreateRoleRequest createRoleRequest) {
//        String jwt = request.getHeader("Authorization");
//        List<String> authorities = jwtDecoder.getAuthorities(jwt);
//
//        if(!authorities.contains("ROLE_ADMIN")) {
//            return new ResponseEntity<>("접근 권한이 없습니다.",HttpStatus.NOT_ACCEPTABLE);
//        }

        try{
            CreateRoleResponse createRoleResponse = roleService.createRole(createRoleRequest.getName());
            return new ResponseEntity<>(createRoleResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("이미 존재하는 권한 이름입니다.",HttpStatus.BAD_REQUEST);
        }

    }


}
