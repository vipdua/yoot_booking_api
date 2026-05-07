package com.yoot.booking.api.dto.bookingservice;

import java.math.BigDecimal;

public record BookingServiceResponseDTO(

        Long id,

        String name,

        String description,

        Integer duration,

        BigDecimal price,

        String imageUrl,

        Boolean isActive,

        Long categoryId,

        String categoryName

) {}