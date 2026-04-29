package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.auth.*;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResultDTO<?> register(@RequestBody UserRegisterRequestDTO request) {
        authService.register(request);
        return ResultDTO.success(null, "Đăng ký tài khoản thành công");
    }

    @PostMapping("/login")
    public ResultDTO<AuthResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        return ResultDTO.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResultDTO<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request) {
        return ResultDTO.success(authService.refresh(request));
    }
}