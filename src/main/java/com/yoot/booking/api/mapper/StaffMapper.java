package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.staff.StaffCreateDTO;
import com.yoot.booking.api.dto.staff.StaffResponseDTO;
import com.yoot.booking.api.dto.staff.StaffUpdateDTO;
import com.yoot.booking.api.entity.Staff;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaffMapper {

    // ================= CREATE =================
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    Staff toEntity(StaffCreateDTO dto);

    // ================= RESPONSE =================
    StaffResponseDTO toDTO(Staff entity);

    // ================= UPDATE =================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    void updateEntityFromDTO(
            StaffUpdateDTO dto,
            @MappingTarget Staff entity
    );
}