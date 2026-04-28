package com.yoot.booking.api.dto.staff;

import com.yoot.booking.api.dto.bookingservice.BookingServiceResponseDTO;

import java.util.List;

public record StaffResponseDTO(
        Long id,
        String name,
        String specialization,
        Boolean isActive,
        List<BookingServiceResponseDTO> services
) {}
