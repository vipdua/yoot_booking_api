package com.yoot.booking.api.dto.Common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PagingRequestDTO {
    @Schema(defaultValue = "1", example = "1")
    private int page = 1;

    @Schema(defaultValue = "10", example = "10")
    private int size = 10;

    private String sortBy; // sắp xếp theo trường nào

    @Schema(defaultValue = "desc", example = "desc")
    private String sortDir = "desc"; // hướng sắp xếp: "asc" hoặc "desc", mặc định là "desc" là giảm dần

    private static final int MAX_SIZE = 100;

    public Pageable toPageable() {

        int safePage = Math.max(page - 1, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_SIZE);

        String safeSortDir = (sortDir == null || sortDir.trim().isEmpty())
                ? "desc"
                : sortDir.trim();

        Sort sort;

        if (sortBy == null || sortBy.isBlank()) {
            sort = Sort.unsorted();
        } else {

            if (!isValidSortField(sortBy)) {
                sort = Sort.unsorted(); // fallback
            } else {
                sort = Sort.by(
                        safeSortDir.equalsIgnoreCase("asc")
                                ? Sort.Direction.ASC
                                : Sort.Direction.DESC,
                        sortBy
                );
            }
        }

        return PageRequest.of(safePage, safeSize, sort);
    }

//    private boolean isValidSortField(String field) {
//        return switch (field) {
//            case "id", "name", "price", "duration", "isActive" -> true;
//            default -> false;
//        };
//    }

    private boolean isValidSortField(String field) {
        return switch (field) {
            case "id", "startTime", "endTime", "isActive" -> true;
            default -> false;
        };
    }
}