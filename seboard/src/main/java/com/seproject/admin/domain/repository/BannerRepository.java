package com.seproject.admin.domain.repository;

import com.seproject.admin.controller.dto.banner.AdminBannerResponse;
import com.seproject.admin.domain.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("select new com.seproject.admin.controller.dto.banner.AdminBannerResponse(b) from Banner b")
    Page<AdminBannerResponse> findAllBanner(Pageable pageable);
    @Query(value = "select new com.seproject.admin.controller.dto.banner.AdminBannerResponse(b) from Banner b where b.startDate <= current_date and b.endDate >= current_date",
    countQuery = "select count(b) from Banner b where b.startDate <= current_date and b.endDate >= current_date")
    Page<AdminBannerResponse> findActiveBanners(Pageable pageable);

    @Query(value = "select new com.seproject.admin.controller.dto.banner.AdminBannerResponse(b) from Banner b where b.startDate > current_date or b.endDate < current_date",
    countQuery = "select count(b) from Banner b where b.startDate > current_date or b.endDate < current_date")
    Page<AdminBannerResponse> findUnActiveBanners(Pageable pageable);
}
