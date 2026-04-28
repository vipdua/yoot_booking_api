package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.schedule.ScheduleCreateDTO;
import com.yoot.booking.api.dto.schedule.ScheduleResponseDTO;
import com.yoot.booking.api.dto.schedule.ScheduleUpdateDTO;
import com.yoot.booking.api.entity.Schedule;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScheduleMapper {

    // staff được gán thủ công trong service
    @Mapping(target = "staff", ignore = true)
    Schedule toEntity(ScheduleCreateDTO dto);

    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.name")
    ScheduleResponseDTO toDTO(Schedule entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "staff", ignore = true)
    void updateEntityFromDTO(ScheduleUpdateDTO dto, @MappingTarget Schedule entity);
}
