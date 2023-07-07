package com.seproject.board.bulletin.controller;

import com.seproject.board.bulletin.domain.repository.BannerRepository;
import com.seproject.board.bulletin.controller.dto.BannerResponse;
import lombok.RequiredArgsConstructor;
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
