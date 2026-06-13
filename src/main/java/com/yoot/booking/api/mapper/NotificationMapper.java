package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.notification.NotificationResponseDTO;
import com.yoot.booking.api.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    NotificationResponseDTO toDTO(Notification entity);
}