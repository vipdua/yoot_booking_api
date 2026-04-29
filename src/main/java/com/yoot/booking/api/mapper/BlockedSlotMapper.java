package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.blockedslot.BlockedSlotResponseDTO;
import com.yoot.booking.api.entity.BlockedSlot;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlockedSlotMapper {

    @Mapping(target = "staffId", source = "staff.id")
    @Mapping(target = "staffName", source = "staff.name")
    BlockedSlotResponseDTO toDTO(BlockedSlot entity);
}
