package com.yoot.booking.api.dto.appointment;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(

        Long id,

        Long staffId,
        String staffName,

        Long serviceId,
        String serviceName,

        LocalDateTime startTime,
        LocalDateTime endTime,

        String status
) {}