package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.menu.*;
import com.yoot.booking.api.entity.Menu;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuMapper {

    // CREATE
    @Mapping(target = "parent", ignore = true)
    Menu toEntity(MenuCreateDTO dto);

    // RESPONSE
    @Mapping(target = "parentId", source = "parent.id")
    MenuResponseDTO toDTO(Menu entity);

    // UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "parent", ignore = true)
    void updateEntity(MenuUpdateDTO dto, @MappingTarget Menu entity);
}