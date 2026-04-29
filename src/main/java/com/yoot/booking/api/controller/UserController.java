package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.user.*;
import com.yoot.booking.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResultDTO<UserResponseDTO> getCurrentUser() {
        return userService.getCurrentUserInfo();
    }

    @PutMapping("/profile")
    public ResultDTO<UserResponseDTO> updateProfile(@RequestBody UserUpdateDTO dto) {
        return userService.updateProfile(dto);
    }

    @PutMapping("/change-password")
    public ResultNoDataDTO changePassword(@RequestBody ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }
}