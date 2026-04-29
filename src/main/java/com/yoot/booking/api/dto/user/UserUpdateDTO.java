package com.yoot.booking.api.dto.user;

public record UserUpdateDTO(
        String fullName,
        String phone,
        String address,
        String avatar
) {}