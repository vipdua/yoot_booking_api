package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.appointment.AppointmentResponseDTO;
import com.yoot.booking.api.entity.Appointment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userPhone", source = "user.phone")

    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.name")

    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")

    @Mapping(target = "status", source = "status")
    AppointmentResponseDTO toDTO(Appointment entity);
}