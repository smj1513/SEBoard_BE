package com.seproject.board.bulletin.controller.dto;

import com.seproject.board.bulletin.domain.model.Banner;
import com.seproject.file.controller.dto.FileMetaDataResponse.FileMetaDataElement;
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
