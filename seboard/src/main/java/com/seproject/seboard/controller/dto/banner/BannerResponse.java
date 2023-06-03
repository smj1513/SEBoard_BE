package com.seproject.seboard.controller.dto.banner;

import com.seproject.admin.domain.Banner;
import com.seproject.seboard.controller.dto.FileMetaDataResponse;
import lombok.Data;


@Data
public class BannerResponse {
    private FileMetaDataResponse.FileMetaDataElement fileMetaData;
    private String bannerUrl;

    public BannerResponse(Banner banner){
        this.fileMetaData = new FileMetaDataResponse.FileMetaDataElement(banner.getFileMetaData());
        this.bannerUrl = banner.getBannerUrl();
    }
}
