package com.yoot.booking.api.dto.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleUpdateDTO(
        Long staffId,
        LocalDate workDate,
        LocalTime startTime,
        LocalTime endTime
) {}
