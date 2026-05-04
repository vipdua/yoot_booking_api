package com.yoot.booking.api.dto.bookingservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BookingServiceCreateDTO(
    @NotBlank(message = "Tên không được để trống")
    String name,

    @NotNull(message = "Duration không được null")
    Integer duration,

    @NotNull(message = "Price không được null")
    BigDecimal price,

    @NotNull(message = "CategoryId không được null")
    Long categoryId
) {}