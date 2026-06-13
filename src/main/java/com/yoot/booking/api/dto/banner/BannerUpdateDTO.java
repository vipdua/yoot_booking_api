package com.yoot.booking.api.dto.banner;

import com.yoot.booking.api.entity.BannerPosition;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record BannerUpdateDTO(

        String title,

        String description,

        @Schema(type = "string", format = "binary")
        MultipartFile imageFile,

        @Schema(type = "string", format = "binary")
        MultipartFile videoFile,

        String ctaText,

        String ctaLink,

        BannerPosition position,

        Boolean isActive,

        LocalDateTime startDate,

        LocalDateTime endDate

) {}