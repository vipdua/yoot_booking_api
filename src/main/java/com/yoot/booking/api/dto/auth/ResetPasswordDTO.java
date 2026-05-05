package com.yoot.booking.api.dto.auth;

public record ResetPasswordDTO(
        String email,
        String otp,
        String newPassword
) {}