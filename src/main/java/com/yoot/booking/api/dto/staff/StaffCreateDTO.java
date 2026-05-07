package com.yoot.booking.api.dto.staff;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record StaffCreateDTO(
        @NotBlank(message = "Tên nhân viên không được để trống")
        String name,
        String specialization,
        String description,
        String position,
        Integer experienceYears,
        MultipartFile avatar,
        List<Long> serviceIds
) {}