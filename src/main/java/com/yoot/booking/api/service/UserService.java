package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.user.*;

public interface UserService {

    ResultDTO<UserResponseDTO> getCurrentUserInfo();

    ResultDTO<UserResponseDTO> updateProfile(UserUpdateDTO dto);

    ResultNoDataDTO changePassword(ChangePasswordDTO dto);
}