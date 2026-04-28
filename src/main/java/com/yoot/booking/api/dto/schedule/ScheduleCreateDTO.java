package com.yoot.booking.api.dto.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleCreateDTO(

        @NotNull(message = "staffId không được để trống")
        Long staffId,

        @NotNull(message = "workDate không được để trống")
        LocalDate workDate,

        @NotNull(message = "startTime không được để trống")
        LocalTime startTime,

        @NotNull(message = "endTime không được để trống")
        LocalTime endTime
) {}
