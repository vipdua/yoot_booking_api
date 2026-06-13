package com.yoot.booking.api.dto.staff;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record StaffUpdateDTO(
        String name,
        String specialization,
        String description,
        String position,
        Integer experienceYears,
        Boolean isActive,
        MultipartFile avatar,
        List<Long> serviceIds
) {}