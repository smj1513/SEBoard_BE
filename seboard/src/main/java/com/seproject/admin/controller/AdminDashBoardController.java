package com.seproject.admin.controller;

import com.seproject.admin.service.AdminDashBoardService;
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
    public ResponseEntity<?> retrieveSettings() {
        return new ResponseEntity<>(adminDashBoardService.findDashBoardSettings(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> updateSettings(@RequestBody DashBoardUpdateRequest request) {
        adminDashBoardService.updateDashBoardSetting(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
