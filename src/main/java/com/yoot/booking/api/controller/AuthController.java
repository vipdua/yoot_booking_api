package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.auth.*;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ================= AUTH =================
    @PostMapping("/register")
    public ResultNoDataDTO register(@RequestBody UserRegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResultDTO<AuthResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResultDTO<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request) {
        return ResultDTO.success(authService.refresh(request));
    }

    // ================= OTP - FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResultNoDataDTO forgotPassword(@RequestBody ForgotPasswordDTO request) {
        return authService.forgotPassword(request);
    }

    @PostMapping("/verify-otp")
    public ResultNoDataDTO verifyOtp(@RequestBody VerifyOtpDTO request) {
        return authService.verifyOtp(request);
    }

    @PostMapping("/reset-password")
    public ResultNoDataDTO resetPassword(@RequestBody ResetPasswordDTO request) {
        return authService.resetPassword(request);
    }

    // ================= ACTIVATE ACCOUNT =================
    @PostMapping("/send-activate-otp")
    public ResultNoDataDTO sendActivateOtp(@RequestBody ForgotPasswordDTO request) {
        return authService.sendActivateOtp(request);
    }

    @PostMapping("/activate")
    public ResultNoDataDTO activateAccount(@RequestBody VerifyOtpDTO request) {
        return authService.activateAccount(request);
    }
}