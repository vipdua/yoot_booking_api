package com.yoot.booking.api.dto.staff;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record StaffCreateDTO(
        @NotBlank(message = "Tên nhân viên không được để trống")
        String name,
        String specialization,
        List<Long> serviceIds
) {}
