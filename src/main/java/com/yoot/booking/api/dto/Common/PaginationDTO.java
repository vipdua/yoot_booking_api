package com.yoot.booking.api.dto.Common;

public record PaginationDTO(
        int page,
        int last,
        int limit,
        int total) {
}