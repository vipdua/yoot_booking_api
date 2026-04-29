package com.yoot.booking.api.dto.appointment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRescheduleDTO(

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime
) {}