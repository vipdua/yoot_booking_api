package com.yoot.booking.api.dto.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleResponseDTO(
        Long id,
        Long staffId,
        String staffName,
        LocalDate workDate,
        LocalTime startTime,
        LocalTime endTime
) {}
