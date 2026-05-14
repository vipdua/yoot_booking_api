package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.user.*;
import com.yoot.booking.api.entity.MediaType;
import com.yoot.booking.api.entity.User;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.UserMapper;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.service.FileStorageService;
import com.yoot.booking.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final PaginationMapper paginationMapper;

    @Override
    public ResultListDTO<UserResponseDTO> getAllUsers(PagingRequestDTO request) {

        var pageable = request.toPageable();

        var page = userRepository.findAll(pageable);

        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách user thành công", pagination);
    }

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

        // ================= UPDATE INFO =================
        if (dto.fullName() != null) {
            user.setFullName(dto.fullName());
        }

        if (dto.phone() != null) {
            user.setPhone(dto.phone());
        }

        if (dto.description() != null) {
            user.setDescription(dto.description());
        }

        if (dto.address() != null) {
            user.setAddress(dto.address());
        }

        // ================= AVATAR =================
        if (
                dto.avatar() != null &&
                        !dto.avatar().isEmpty()
        ) {
            // xóa avatar cũ
            if (
                    user.getAvatar() != null && !user.getAvatar().isBlank()
            ) {
                fileStorageService.delete(user.getAvatar(), MediaType.IMAGE);
            }

            // upload avatar mới
            String avatarUrl = fileStorageService.upload(dto.avatar(), "users", MediaType.IMAGE);

            user.setAvatar(avatarUrl);
        }

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

    @Override
    @Transactional
    public ResultDTO<UserResponseDTO> updateRole(Long id, UpdateUserRoleDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setRole(dto.role());

        return ResultDTO.success(mapper.toDTO(user), "Cập nhật role thành công");
    }

    @Override
    @Transactional
    public ResultNoDataDTO banUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại"));

        user.setIsActive(false);

        return ResultNoDataDTO.success("Khóa user thành công");
    }

    @Override
    @Transactional
    public ResultNoDataDTO unbanUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại"));

        user.setIsActive(true);

        return ResultNoDataDTO.success("Mở khóa user thành công");
    }
}