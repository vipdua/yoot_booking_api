package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.BlockedSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BlockedSlotRepository extends JpaRepository<BlockedSlot, Long> {

    // Lấy tất cả block của staff
    List<BlockedSlot> findByStaffId(Long staffId);

    // lấy block theo time range (dùng cho Slot & logic khác)
    List<BlockedSlot> findByStaffIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long staffId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    // check overlap (dùng khi create)
    boolean existsByStaffIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long staffId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}