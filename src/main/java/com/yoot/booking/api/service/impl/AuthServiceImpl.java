package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.auth.*;
import com.yoot.booking.api.entity.Role;
import com.yoot.booking.api.entity.User;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.security.JwtUtil;
import com.yoot.booking.api.service.AuthService;
import com.yoot.booking.api.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @Override
    public ResultNoDataDTO register(UserRegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email này đã được đăng ký sử dụng!");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .isActive(false)
                .build();

        userRepository.save(user);

        return ResultNoDataDTO.success("Đăng ký thành công. Vui lòng kích hoạt tài khoản qua OTP");
    }

    @Override
    public ResultDTO<AuthResponseDTO> login(UserLoginRequestDTO request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!user.getIsActive()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        AuthResponseDTO response = new AuthResponseDTO(accessToken, refreshToken, "Bearer");

        return ResultDTO.success(response, "Đăng nhập thành công");
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

    @Override
    public ResultNoDataDTO forgotPassword(ForgotPasswordDTO request) {

        User user = findByEmail(request.email());

        if (!user.getIsActive()) {
            throw new RuntimeException("Tài khoản chưa kích hoạt");
        }

        otpService.sendOtp(request.email());

        return ResultNoDataDTO.success("OTP đã gửi");
    }

    @Override
    public ResultNoDataDTO verifyOtp(VerifyOtpDTO request) {

        otpService.verifyOtp(request.email(), request.otp());

        return ResultNoDataDTO.success("OTP hợp lệ");
    }

    @Override
    public ResultNoDataDTO resetPassword(ResetPasswordDTO request) {

        otpService.verifyOtp(request.email(), request.otp());

        User user = findByEmail(request.email());

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);

        otpService.clearOtp(request.email());

        return ResultNoDataDTO.success("Đặt lại mật khẩu thành công");
    }

    @Override
    public ResultNoDataDTO sendActivateOtp(ForgotPasswordDTO request) {

        User user = findByEmail(request.email());

        if (user.getIsActive()) {
            throw new RuntimeException("Tài khoản đã kích hoạt");
        }

        otpService.sendOtp(request.email());

        return ResultNoDataDTO.success("OTP kích hoạt đã gửi");
    }

    @Override
    public ResultNoDataDTO activateAccount(VerifyOtpDTO request) {

        otpService.verifyOtp(request.email(), request.otp());

        User user = findByEmail(request.email());

        user.setIsActive(true);

        userRepository.save(user);

        otpService.clearOtp(request.email());

        return ResultNoDataDTO.success("Tài khoản đã kích hoạt");
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }
}
