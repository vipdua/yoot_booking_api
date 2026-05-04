package com.yoot.booking.api.dto.bookingservice;

import java.math.BigDecimal;

public record BookingServiceResponseDTO(
    Long id,
    String name,
    Integer duration,
    BigDecimal price,
    Boolean isActive,

    Long categoryId,
    String categoryName
) {}