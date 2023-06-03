package com.seproject.seboard.controller;

import com.seproject.admin.controller.dto.banner.AdminBannerResponse;
import com.seproject.admin.domain.repository.BannerRepository;
import com.seproject.seboard.controller.dto.banner.BannerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerRepository bannerRepository;

    @GetMapping
    public ResponseEntity<List<BannerResponse>> retrieveActiveBanner(){
        return ResponseEntity.ok(bannerRepository.findBannersForUser());
    }
}
