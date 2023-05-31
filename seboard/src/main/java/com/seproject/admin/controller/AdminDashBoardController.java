package com.seproject.admin.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "관리자 대시보드 관리 API", description = "관리자 메뉴 접근 관리 관리 API")
@AllArgsConstructor
@RequestMapping(value = "/admin/dashboard")
@Controller
public class AdminDashBoardController {


    @GetMapping
    public ResponseEntity<?> retrieveSettings() {



        return new ResponseEntity<>(HttpStatus.OK);
    }
}
