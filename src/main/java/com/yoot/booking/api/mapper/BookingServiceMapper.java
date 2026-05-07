package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.entity.BookingService;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingServiceMapper {

    // ================= RESPONSE =================
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    BookingServiceResponseDTO toDTO(BookingService entity);

    // ================= CREATE =================
    BookingService toEntity(BookingServiceCreateDTO dto);

    // ================= UPDATE =================
    void updateEntityFromDTO(
            BookingServiceUpdateDTO dto,
            @MappingTarget BookingService entity
    );
}