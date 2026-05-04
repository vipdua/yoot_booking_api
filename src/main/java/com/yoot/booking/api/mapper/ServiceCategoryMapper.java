package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.category.*;
import com.yoot.booking.api.entity.ServiceCategory;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceCategoryMapper {

    // Entity → Response
    ServiceCategoryResponseDTO toDTO(ServiceCategory entity);

    // Create DTO → Entity
    @Mapping(target = "id", ignore = true)
    ServiceCategory toEntity(ServiceCategoryCreateDTO dto);

    // Update DTO → Entity (partial update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget ServiceCategory entity, ServiceCategoryUpdateDTO dto);
}