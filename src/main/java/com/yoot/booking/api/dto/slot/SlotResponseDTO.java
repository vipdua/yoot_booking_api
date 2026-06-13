package com.yoot.booking.api.dto.slot;

import java.time.LocalDate;
import java.time.LocalTime;

public record SlotResponseDTO(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Long staffId
) {}