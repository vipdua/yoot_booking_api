package com.yoot.booking.api.dto.bookingservice;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record BookingServiceUpdateDTO(

        String name,

        String description,

        Integer duration,

        BigDecimal price,

        MultipartFile imageFile,

        Boolean isActive,

        Long categoryId

) {}