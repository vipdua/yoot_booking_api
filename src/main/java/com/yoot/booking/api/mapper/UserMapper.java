package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.user.UserResponseDTO;
import com.yoot.booking.api.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDTO toDTO(User entity);
}