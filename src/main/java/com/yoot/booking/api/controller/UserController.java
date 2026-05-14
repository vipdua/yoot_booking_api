package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;

import com.yoot.booking.api.dto.user.ChangePasswordDTO;
import com.yoot.booking.api.dto.user.UpdateUserRoleDTO;
import com.yoot.booking.api.dto.user.UserResponseDTO;
import com.yoot.booking.api.dto.user.UserUpdateDTO;

import com.yoot.booking.api.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ================= GET ALL USERS =================
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultListDTO<UserResponseDTO> getAllUsers(@ModelAttribute PagingRequestDTO request) {
        return userService.getAllUsers(request);
    }

    // ================= GET PROFILE =================
    @GetMapping("/me/profile")
    @PreAuthorize("hasAnyRole('USER', 'STAFF','ADMIN')")
    public ResultDTO<UserResponseDTO> getCurrentUser() {
        return userService.getCurrentUserInfo();
    }

    // ================= UPDATE PROFILE =================
    @PutMapping(value = "/me/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'STAFF','ADMIN')")
    public ResultDTO<UserResponseDTO> updateProfile(@ModelAttribute UserUpdateDTO dto) {
        return userService.updateProfile(dto);
    }

    // ================= CHANGE PASSWORD =================
    @PutMapping("/me/change-password")
    @PreAuthorize("hasAnyRole('USER', 'STAFF','ADMIN')")
    public ResultNoDataDTO changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        return userService.changePassword(dto);
    }

    // ================= UPDATE ROLE =================
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultDTO<UserResponseDTO> updateRole(@PathVariable Long id, @RequestBody @Valid UpdateUserRoleDTO dto) {
        return userService.updateRole(id, dto);
    }

    // ================= BAN USER =================
    @PutMapping("/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultNoDataDTO banUser(@PathVariable Long id) {
        return userService.banUser(id);
    }

    // ================= UNBAN USER =================
    @PutMapping("/{id}/unban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultNoDataDTO unbanUser(@PathVariable Long id) {
        return userService.unbanUser(id);
    }
}