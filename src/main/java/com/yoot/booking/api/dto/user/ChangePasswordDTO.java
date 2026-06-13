package com.yoot.booking.api.dto.user;

public record ChangePasswordDTO(
        String oldPassword,
        String newPassword
) {}