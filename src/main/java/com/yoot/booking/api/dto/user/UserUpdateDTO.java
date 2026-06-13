package com.yoot.booking.api.dto.user;

import org.springframework.web.multipart.MultipartFile;

public record UserUpdateDTO(

        String fullName,

        String phone,

        String description,

        String address,

        MultipartFile avatar
) {}