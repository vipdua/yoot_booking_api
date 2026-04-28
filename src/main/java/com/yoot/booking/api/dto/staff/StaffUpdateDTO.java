package com.yoot.booking.api.dto.staff;

import java.util.List;

public record StaffUpdateDTO(

        // Tất cả field đều nullable để hỗ trợ partial update
        String name,

        String specialization,

        Boolean isActive,

        // Nếu không null → thay thế toàn bộ danh sách service của staff
        List<Long> serviceIds
) {}
