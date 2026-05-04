package com.yoot.booking.api.dto.category;

public record ServiceCategoryResponseDTO(
        Long id,
        String name,
        String description,
        Boolean isActive
) {}