package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.auth.*;

public interface AuthService {

    void register(UserRegisterRequestDTO request);

    AuthResponseDTO login(UserLoginRequestDTO request);

    AuthResponseDTO refresh(RefreshTokenRequestDTO request);
}