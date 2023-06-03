package com.seproject.admin.controller.dto.banner;

import com.seproject.admin.domain.Banner;
import com.seproject.seboard.controller.dto.FileMetaDataResponse;
import com.seproject.seboard.controller.dto.FileMetaDataResponse.FileMetaDataElement;
import com.seproject.seboard.domain.model.common.FileMetaData;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminBannerResponse {
    private Long bannerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private FileMetaDataElement fileMetaData;
    private String bannerUrl;

    public AdminBannerResponse(Banner banner) {
        this.bannerId = banner.getBannerId();
        this.startDate = banner.getStartDate();
        this.endDate = banner.getEndDate();
        this.fileMetaData = new FileMetaDataElement(banner.getFileMetaData());
        this.bannerUrl = banner.getBannerUrl();
    }
}
