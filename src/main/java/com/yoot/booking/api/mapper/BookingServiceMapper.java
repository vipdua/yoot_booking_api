package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.entity.BookingService;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingServiceMapper {

    // ================= CREATE =================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    BookingService toEntity(BookingServiceCreateDTO dto);

    // ================= RESPONSE =================
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    BookingServiceResponseDTO toDTO(BookingService entity);

    // ================= UPDATE =================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDTO(BookingServiceUpdateDTO dto, @MappingTarget BookingService entity);
}