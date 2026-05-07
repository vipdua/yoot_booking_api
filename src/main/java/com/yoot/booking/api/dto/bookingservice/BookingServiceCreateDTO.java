package com.yoot.booking.api.dto.bookingservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record BookingServiceCreateDTO(

        @NotBlank(message = "Tên không được để trống")
        String name,

        @NotBlank(message = "Mô tả không được để trống")
        String description,

        @NotNull(message = "Duration không được null")
        Integer duration,

        @NotNull(message = "Price không được null")
        BigDecimal price,

        MultipartFile imageFile,

        @NotNull(message = "CategoryId không được null")
        Long categoryId

) {}