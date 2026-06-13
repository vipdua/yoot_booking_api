package com.yoot.booking.api.dto.banner;

import com.yoot.booking.api.entity.BannerPosition;

import java.time.LocalDateTime;

public record BannerResponseDTO(

        Long id,

        String title,

        String description,

        String imageUrl,

        String videoUrl,

        String ctaText,

        String ctaLink,

        BannerPosition position,

        Boolean isActive,

        Integer orderIndex,

        LocalDateTime startDate,

        LocalDateTime endDate

) {}