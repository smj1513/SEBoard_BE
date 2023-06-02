package com.seproject.admin.controller;

import com.seproject.admin.controller.dto.banner.AdminBannerRequest;
import com.seproject.admin.controller.dto.banner.AdminBannerRequest.AdminCreateBannerRequest;
import com.seproject.admin.service.AdminBannerAppService;
import com.seproject.seboard.controller.dto.MessageResponse;
import com.seproject.seboard.controller.dto.MessageResponse.CreateAndUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/banner")
public class AdminBannerController {
    private final AdminBannerAppService adminBannerAppService;

    @PostMapping
    public ResponseEntity<CreateAndUpdateMessage> createBanner(@RequestBody AdminCreateBannerRequest request){

        Long bannerId = adminBannerAppService.createBanner(request.getStartDate(), request.getEndDate(), request.getBannerUrl(), request.getFileMetaDataId());

        return ResponseEntity.ok(CreateAndUpdateMessage.of(bannerId, "배너 생성 성공"));
    }
}
