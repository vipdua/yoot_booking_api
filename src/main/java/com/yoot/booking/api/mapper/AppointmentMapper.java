package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.appointment.AppointmentResponseDTO;
import com.yoot.booking.api.entity.Appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {

    // ================= USER =================
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userPhone", source = "user.phone")

    // ================= CUSTOMER =================
    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "customerPhone", source = "customerPhone")
    @Mapping(target = "customerEmail", source = "customerEmail")
    @Mapping(target = "note", source = "note")

    // ================= STAFF =================
    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.name")

    // ================= SERVICE =================
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "serviceName", source = "service.name")

    // ================= PRICE =================
    @Mapping(target = "totalPrice", source = "totalPrice")

    // ================= STATUS =================
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentStatus", source = "paymentStatus")

    AppointmentResponseDTO toDTO(Appointment entity);
}