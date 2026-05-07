package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Banner;
import com.yoot.booking.api.entity.BannerPosition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    // lấy banner active theo position + thời gian
    List<Banner> findByPositionAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByOrderIndexAsc(
            BannerPosition position,
            LocalDateTime now1,
            LocalDateTime now2
    );

    // lấy banner theo position để auto order
    List<Banner> findByPositionOrderByOrderIndexAsc(BannerPosition position);
}