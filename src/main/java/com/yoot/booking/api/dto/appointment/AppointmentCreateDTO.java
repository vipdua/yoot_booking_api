package com.yoot.booking.api.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateDTO(

        // ================= STAFF =================
        @NotNull
        Long staffId,

        // ================= SERVICE =================
        @NotNull
        Long serviceId,

        // ================= CUSTOMER =================
        @NotBlank
        String customerName,

        @NotBlank
        String customerPhone,

        String customerEmail,

        String note,

        // ================= TIME =================
        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime
) {}