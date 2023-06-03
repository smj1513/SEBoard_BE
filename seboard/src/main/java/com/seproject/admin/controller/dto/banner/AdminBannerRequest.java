package com.seproject.admin.controller.dto.banner;

import lombok.Data;

import java.time.LocalDate;

public class AdminBannerRequest {
    @Data
    public static class AdminCreateBannerRequest {
        private LocalDate startDate;
        private LocalDate endDate;
        private String bannerUrl;
        private Long fileMetaDataId;
    }
}
