package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Page<Staff> findByServicesId(Long serviceId, Pageable pageable);

    Page<Staff> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Staff> findByIsActiveAndServicesId(Boolean isActive, Long serviceId, Pageable pageable);

    Optional<Staff> findByIdAndIsActiveTrue(Long id);
    /**
     * Lấy staff rảnh: là staff active và KHÔNG có appointment CONFIRMED/PENDING
     * nào overlap với khoảng [start, end)
     */
    @Query("""
        SELECT DISTINCT s FROM Staff s
        JOIN s.services sv
        WHERE s.isActive = true
        AND sv.id = :serviceId
        AND s.id NOT IN (
            SELECT a.staff.id FROM Appointment a
            WHERE a.status IN ('CONFIRMED', 'PENDING')
            AND a.startTime < :end
            AND a.endTime > :start
        )
        """)
    Page<Staff> findAvailableStaffByService(
            @Param("serviceId") Long serviceId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
}