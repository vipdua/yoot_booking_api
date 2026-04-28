package com.yoot.booking.api.dto.bookingservice;

import java.math.BigDecimal;

public record BookingServiceRequestDTO(
    String name,
    Integer duration,
    BigDecimal price
) {}