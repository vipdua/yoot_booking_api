package com.yoot.booking.api.dto.appointment;

import com.yoot.booking.api.entity.AppointmentStatus;
import com.yoot.booking.api.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentResponseDTO(

        Long id,

        // ================= USER =================
        Long userId,
        String userName,
        String userEmail,
        String userPhone,

        // ================= CUSTOMER =================
        String customerName,
        String customerPhone,
        String customerEmail,
        String note,

        // ================= STAFF =================
        Long staffId,
        String staffName,

        // ================= SERVICE =================
        Long serviceId,
        String serviceName,

        // ================= PRICE =================
        BigDecimal totalPrice,

        // ================= TIME =================
        LocalDateTime startTime,
        LocalDateTime endTime,

        // ================= STATUS =================
        AppointmentStatus status,

        PaymentStatus paymentStatus

) {}