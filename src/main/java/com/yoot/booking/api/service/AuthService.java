package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.auth.*;

public interface AuthService {

    ResultNoDataDTO register(UserRegisterRequestDTO request);

    ResultDTO<AuthResponseDTO> login(UserLoginRequestDTO request);

    AuthResponseDTO refresh(RefreshTokenRequestDTO request);

    // OTP
    ResultNoDataDTO forgotPassword(ForgotPasswordDTO request);

    ResultNoDataDTO verifyOtp(VerifyOtpDTO request);

    ResultNoDataDTO resetPassword(ResetPasswordDTO request);

    // activate
    ResultNoDataDTO sendActivateOtp(ForgotPasswordDTO request);

    ResultNoDataDTO activateAccount(VerifyOtpDTO request);
}