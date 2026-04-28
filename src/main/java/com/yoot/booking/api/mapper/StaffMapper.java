package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.staff.StaffCreateDTO;
import com.yoot.booking.api.dto.staff.StaffResponseDTO;
import com.yoot.booking.api.dto.staff.StaffUpdateDTO;
import com.yoot.booking.api.entity.Staff;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StaffMapper {

    // Không map services ở đây — xử lý trong service
    @Mapping(target = "services", ignore = true)
    Staff toEntity(StaffCreateDTO dto);

    StaffResponseDTO toDTO(Staff entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "services", ignore = true)
    void updateEntityFromDTO(StaffUpdateDTO dto, @MappingTarget Staff entity);
}
