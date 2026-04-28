package com.yoot.booking.api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "Refresh token không được để trống")
        String refreshToken
) {}