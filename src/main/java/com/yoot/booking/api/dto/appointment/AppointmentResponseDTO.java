package com.yoot.booking.api.dto.appointment;

import com.yoot.booking.api.entity.AppointmentStatus;
import com.yoot.booking.api.entity.PaymentStatus;

import java.time.LocalDateTime;

public record AppointmentResponseDTO(

        Long id,

        Long staffId,
        String staffName,

        Long serviceId,
        String serviceName,

        LocalDateTime startTime,
        LocalDateTime endTime,

        AppointmentStatus status,
        PaymentStatus paymentStatus

) {}