package com.yoot.booking.api.dto.banner;

import com.yoot.booking.api.entity.BannerPosition;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record BannerCreateDTO(

        String title,

        String description,

        @Schema(type = "string", format = "binary")
        MultipartFile imageFile,

        @Schema(type = "string", format = "binary")
        MultipartFile videoFile,

        String ctaText,

        String ctaLink,

        @NotNull(message = "Position không được trống")
        BannerPosition position,

        @NotNull(message = "isActive không được trống")
        Boolean isActive,

        @NotNull(message = "startDate không được trống")
        LocalDateTime startDate,

        @NotNull(message = "endDate không được trống")
        LocalDateTime endDate

) {}