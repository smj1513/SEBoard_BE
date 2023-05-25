package com.seproject.seboard.controller;

import com.seproject.seboard.application.ProfileAppService;
import com.seproject.seboard.controller.dto.profile.ProfileResponse;
import com.seproject.seboard.controller.dto.profile.ProfileResponse.ProfileInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileAppService profileAppService;
    @GetMapping("/{loginId}")
    public ResponseEntity<ProfileInfoResponse> retrieveProfileInfo(@PathVariable String loginId){
        ProfileInfoResponse profileInfoResponse = profileAppService.retrieveProfileInfo(loginId);
        return ResponseEntity.ok().body(profileInfoResponse);
    }

}
