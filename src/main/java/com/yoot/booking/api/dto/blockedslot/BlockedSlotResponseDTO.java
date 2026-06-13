package com.yoot.booking.api.dto.blockedslot;

import java.time.LocalDateTime;

public record BlockedSlotResponseDTO(
        Long id,
        Long staffId,
        String staffName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String reason
) {}