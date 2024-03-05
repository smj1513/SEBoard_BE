package com.seproject.admin.settings.controller;

import com.seproject.admin.settings.controller.dto.LoginSettingDTO;
import com.seproject.admin.settings.service.LoginSettingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/admin/loginSettings")
public class LoginSettingController {

    private final LoginSettingService loginSettingService;

    @Operation(summary = "로그인 제한 시간, 시도 횟수 조회")
    @GetMapping
    public ResponseEntity<LoginSettingDTO> findLoginSetting() {
        LoginSettingDTO response = new LoginSettingDTO(loginSettingService.getLoginLimitTime(), loginSettingService.getLoginTryCount());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "로그인 제한 시간, 시도 횟수 수정")
    @PutMapping
    public ResponseEntity<Void> updateLoginSetting(@RequestBody LoginSettingDTO request) {
        loginSettingService.update(request.getLoginLimitTime(), request.getLoginTryCount());
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
