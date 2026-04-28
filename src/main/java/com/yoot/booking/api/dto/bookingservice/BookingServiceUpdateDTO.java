package com.yoot.booking.api.dto.bookingservice;

import java.math.BigDecimal;

public record BookingServiceUpdateDTO(
    String name,
    Integer duration,
    BigDecimal price,
    Boolean isActive
) {}