package com.yoot.booking.api.dto.user;

import com.yoot.booking.api.entity.Role;

public record UserResponseDTO(

        Long id,
        String email,
        Role role,
        Boolean isActive
) {}