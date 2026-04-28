package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.auth.*;
import com.yoot.booking.api.entity.Role;
import com.yoot.booking.api.entity.User;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.security.JwtUtil;
import com.yoot.booking.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email này đã được đăng ký sử dụng!");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .isActive(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponseDTO login(UserLoginRequestDTO request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new AuthResponseDTO(accessToken, refreshToken, "Bearer");
    }

    @Override
    public AuthResponseDTO refresh(RefreshTokenRequestDTO request) {

        if (!jwtUtil.isTokenValid(request.refreshToken())) {
            throw new RuntimeException("refresh token không hợp lệ!");
        }

        String email = jwtUtil.extractEmail(request.refreshToken());

        // RefreshToken không mang role claim → dùng "USER" làm fallback
        // AccessToken mới sẽ được sinh lại đúng role nếu cần login lại
        String role = jwtUtil.extractRole(request.refreshToken());
        if (role == null || role.isBlank()) {
            // Tra cứu role từ DB để đảm bảo chính xác
            role = userRepository.findByEmail(email)
                    .map(u -> u.getRole().name())
                    .orElse("USER");
        }

        String newAccessToken = jwtUtil.generateToken(email, role);

        return new AuthResponseDTO(
                newAccessToken,
                request.refreshToken(),
                "Bearer"
        );
    }
}