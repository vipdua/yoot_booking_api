package com.yoot.booking.api.dto.auth;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType
) {}