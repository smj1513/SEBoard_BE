package com.seproject.admin.controller;

import com.seproject.admin.service.AdminDashBoardService;
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

import static com.seproject.admin.controller.dto.DashBoardDTO.*;

@Tag(name = "관리자 대시보드 관리 API", description = "관리자 메뉴 접근 관리 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin/dashboard")
@RestController
public class AdminDashBoardController {

    private final AdminDashBoardService adminDashBoardService;
    @GetMapping
    @Operation(summary = "대시보드 세팅 조회", description = "대시보드에 설정된 옵션을 조회한다.")
    @ApiResponses({
            @ApiResponse(content = @Content(schema = @Schema(implementation = DashBoardRetrieveResponse.class)), responseCode = "200", description = "조회 성공"),
    })
    public ResponseEntity<?> retrieveSettings() {
        return new ResponseEntity<>(adminDashBoardService.findDashBoardSettings(),HttpStatus.OK);
    }

    @Operation(summary = "대시보드 세팅 수정", description = "대시보드에 옵션을 새로 설정한다.")
    @PostMapping
    public ResponseEntity<?> updateSettings(@RequestBody DashBoardUpdateRequest request) {
        adminDashBoardService.updateDashBoardSetting(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
