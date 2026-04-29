package com.yoot.booking.api.dto.appointment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentCreateDTO(

        @NotNull
        Long staffId,

        @NotNull
        Long serviceId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime
) {}