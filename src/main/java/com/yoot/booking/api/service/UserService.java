package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.user.*;

public interface UserService {
    ResultListDTO<UserResponseDTO> getAllUsers(PagingRequestDTO request);

    ResultDTO<UserResponseDTO> getCurrentUserInfo();

    ResultDTO<UserResponseDTO> updateProfile(UserUpdateDTO dto);

    ResultNoDataDTO changePassword(ChangePasswordDTO dto);

    ResultDTO<UserResponseDTO> updateRole(Long id, UpdateUserRoleDTO dto);

    ResultNoDataDTO banUser(Long id);

    ResultNoDataDTO unbanUser(Long id);
}