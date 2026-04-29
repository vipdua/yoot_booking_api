package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.user.*;
import com.yoot.booking.api.entity.User;
import com.yoot.booking.api.mapper.UserMapper;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // Lấy user từ JWT
    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    //  GET/me
    @Override
    public ResultDTO<UserResponseDTO> getCurrentUserInfo() {

        User user = getCurrentUser();

        return ResultDTO.success(mapper.toDTO(user), "Lấy thông tin user thành công");
    }

    // UPDATE PROFILE
    @Override
    @Transactional
    public ResultDTO<UserResponseDTO> updateProfile(UserUpdateDTO dto) {

        User user = getCurrentUser();

        if (dto.fullName() != null) user.setFullName(dto.fullName());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.address() != null) user.setAddress(dto.address());
        if (dto.avatar() != null) user.setAvatar(dto.avatar());

        return ResultDTO.success(mapper.toDTO(user), "Cập nhật hồ sơ thành công");
    }

    // CHANGE PASSWORD
    @Override
    @Transactional
    public ResultNoDataDTO changePassword(ChangePasswordDTO dto) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));

        return ResultNoDataDTO.success("Đổi mật khẩu thành công");
    }
}