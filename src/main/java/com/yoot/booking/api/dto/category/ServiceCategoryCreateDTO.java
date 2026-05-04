package com.yoot.booking.api.dto.category;

import jakarta.validation.constraints.NotBlank;

public record ServiceCategoryCreateDTO(

        @NotBlank(message = "Tên category không được để trống")
        String name,

        String description
) {}