package com.yoot.booking.api.dto.user;

import com.yoot.booking.api.entity.Role;

public record UserResponseDTO(

        Long id,

        String fullName,

        String email,

        String phone,

        String description,

        String address,

        String avatar,

        Role role,

        Boolean isActive
) {}