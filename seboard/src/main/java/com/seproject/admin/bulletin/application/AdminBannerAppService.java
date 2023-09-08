package com.seproject.admin.bulletin.application;

import com.seproject.account.account.domain.Account;
import com.seproject.account.utils.SecurityUtils;
import com.seproject.admin.bulletin.controller.dto.BannerDTO.BannerResponse;
import com.seproject.admin.bulletin.persistence.AdminBannerQueryRepository;
import com.seproject.board.bulletin.service.BannerService;
import com.seproject.error.errorCode.ErrorCode;
import com.seproject.error.exception.InvalidAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.seproject.admin.bulletin.controller.dto.BannerDTO.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBannerAppService {
    private final BannerService bannerService;
    private final AdminBannerQueryRepository adminBannerQueryRepository;

    public List<BannerResponse> retrieveBanner( Boolean isActive){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities()
//                .stream()
//                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        if(isActive==null){
            return adminBannerQueryRepository.findAllBanner();
        }else if(isActive){
            return adminBannerQueryRepository.findActiveBanners(LocalDate.now());
        }else{
            return adminBannerQueryRepository.findUnActiveBanners(LocalDate.now());
        }
    }

    @Transactional
    public Long createBanner(CreateBannerRequest request){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin) {
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        String bannerUrl = request.getBannerUrl();
        Long fileMetaDataId = request.getFileMetaDataId();

        Long bannerId = bannerService.createBanner(startDate, endDate, bannerUrl, fileMetaDataId);
        return bannerId;
    }

    @Transactional
    public void updateBanner(Long bannerId, UpdateBannerRequest request){

//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        String bannerUrl = request.getBannerUrl();
        Long fileMetaDataId = request.getFileMetaDataId();

        bannerService.updateBanner(bannerId,startDate,endDate,bannerUrl,fileMetaDataId);
    }

    @Transactional
    public void deleteBanner(Long bannerId){
//        Account account = SecurityUtils.getAccount()
//                .orElseThrow(() -> new InvalidAuthorizationException(ErrorCode.NOT_LOGIN));
//
//        boolean isAdmin = account.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//
//        if(!isAdmin){
//            throw new InvalidAuthorizationException(ErrorCode.ACCESS_DENIED);
//        }

        bannerService.deleteBanner(bannerId);
    }

}
