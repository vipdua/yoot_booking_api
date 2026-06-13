package com.yoot.booking.api.dto.blockedslot;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BlockedSlotCreateDTO(
        @NotNull(message = "staffId không được để trống")
        Long staffId,
        @NotNull(message = "startTime không được để trống")
        LocalDateTime startTime,
        @NotNull(message = "endTime không được để trống")
        LocalDateTime endTime,
        String reason
) {}
