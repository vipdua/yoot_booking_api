package com.yoot.booking.api.dto.category;

public record ServiceCategoryUpdateDTO(
        String name,
        String description,
        Boolean isActive
) {}