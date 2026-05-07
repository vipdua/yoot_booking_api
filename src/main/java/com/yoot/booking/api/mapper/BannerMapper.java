package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.banner.BannerResponseDTO;
import com.yoot.booking.api.entity.Banner;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BannerMapper {

    // RESPONSE
    BannerResponseDTO toDTO(Banner entity);
}