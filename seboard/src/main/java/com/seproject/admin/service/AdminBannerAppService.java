package com.seproject.admin.service;

import com.seproject.account.model.account.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.controller.dto.banner.AdminBannerResponse;
import com.seproject.admin.domain.Banner;
import com.seproject.admin.domain.repository.BannerRepository;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import com.seproject.error.exception.NoSuchResourceException;
import com.seproject.seboard.application.FileAppService;
import com.seproject.seboard.domain.model.common.FileMetaData;
import com.seproject.seboard.domain.repository.commons.FileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminBannerAppService {
    private final BannerRepository bannerRepository;
    private final FileMetaDataRepository fileMetaDataRepository;
    private final FileAppService fileAppService;

    public Page<AdminBannerResponse> retrieveBanner(Pageable pageable, Boolean isActive){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        if(isActive==null){
            return bannerRepository.findAllBanner(pageable);
        }else if(isActive){
            return bannerRepository.findActiveBanners(pageable);
        }else{
            return bannerRepository.findUnActiveBanners(pageable);
        }
    }

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

    public Long updateBanner(Long bannerId, LocalDate startDate, LocalDate endDate, String bannerUrl, Long fileMetaDataId){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_BANNER));

        if(!banner.getBannerId().equals(bannerId)){
            fileAppService.deleteFile(banner.getFileMetaData().getFileMetaDataId());
        }

        FileMetaData fileMetaData = fileMetaDataRepository.findById(fileMetaDataId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_FILE));

        banner.updateBanner(startDate, endDate, fileMetaData, bannerUrl);

        return banner.getBannerId();
    }

    public void deleteBanner(Long bannerId){
        Account account = SecurityUtils.getAccount()
                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));

        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin){
            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new NoSuchResourceException(ErrorCode.NOT_EXIST_BANNER));

        fileAppService.deleteFile(banner.getFileMetaData().getFileMetaDataId());
        bannerRepository.delete(banner);
    }

}
