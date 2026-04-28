package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.entity.BookingService;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingServiceMapper {

    BookingService toEntity(BookingServiceCreateDTO dto);

    BookingServiceResponseDTO toDTO(BookingService entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BookingServiceUpdateDTO dto, @MappingTarget BookingService entity);
}