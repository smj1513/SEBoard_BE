package com.seproject.admin.service;

import com.seproject.account.model.account.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.domain.Banner;
import com.seproject.admin.domain.repository.BannerRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminBannerAppService {
    private final BannerRepository bannerRepository;
    private final FileMetaDataRepository fileMetaDataRepository;

    public Long createBanner(LocalDate startDate, LocalDate endDate, String bannerUrl, Long fileMetaDataId){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_FILE));


        Banner banner = new Banner(startDate, endDate, fileMetaData, bannerUrl);

        bannerRepository.save(banner);

        return banner.getBannerId();
    }

}
