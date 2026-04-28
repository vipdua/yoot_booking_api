package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.Common.PaginationDTO;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PaginationMapper {

    default PaginationDTO toPagination(Page<?> page) {
        if (page == null)
            return null;

        return new PaginationDTO(
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getSize(),
                (int) page.getTotalElements());
    }
}
