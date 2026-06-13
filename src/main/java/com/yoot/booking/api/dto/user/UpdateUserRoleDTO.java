package com.yoot.booking.api.dto.user;

import com.yoot.booking.api.entity.Role;

import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleDTO(

        @NotNull
        Role role
) {}