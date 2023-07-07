package com.seproject.board.bulletin.controller;

import com.seproject.board.bulletin.controller.dto.AdminBannerRequest.AdminCreateBannerRequest;
import com.seproject.board.bulletin.controller.dto.AdminBannerResponse;
import com.seproject.board.bulletin.application.AdminBannerAppService;
import com.seproject.board.common.controller.dto.MessageResponse;
import com.seproject.board.common.controller.dto.MessageResponse.CreateAndUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/banners")
public class AdminBannerController {
    private final AdminBannerAppService adminBannerAppService;

    @PostMapping
    public ResponseEntity<CreateAndUpdateMessage> createBanner(@RequestBody AdminCreateBannerRequest request){

        Long bannerId = adminBannerAppService.createBanner(request.getStartDate(), request.getEndDate(), request.getBannerUrl(), request.getFileMetaDataId());

        return ResponseEntity.ok(CreateAndUpdateMessage.of(bannerId, "배너 생성 성공"));
    }

    @GetMapping
    public ResponseEntity<Page<AdminBannerResponse>> retrieveBanner(@RequestParam(required = false) Boolean isActive,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int perPage){
        Page<AdminBannerResponse> res = adminBannerAppService.retrieveBanner(PageRequest.of(page,perPage), isActive);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{bannerId}")
    public ResponseEntity<CreateAndUpdateMessage> updateBanner(@PathVariable Long bannerId, @RequestBody AdminCreateBannerRequest request){
        adminBannerAppService.updateBanner(bannerId, request.getStartDate(), request.getEndDate(), request.getBannerUrl(), request.getFileMetaDataId());
        return ResponseEntity.ok(CreateAndUpdateMessage.of(bannerId, "배너 수정 성공"));
    }

    @DeleteMapping("/{bannerId}")
    public ResponseEntity<MessageResponse> deleteBanner(@PathVariable Long bannerId){
        adminBannerAppService.deleteBanner(bannerId);
        return ResponseEntity.ok(MessageResponse.of("배너 삭제 성공"));
    }
}
