package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Appointment;
import com.yoot.booking.api.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByStaffIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long staffId,
            List<AppointmentStatus> statuses,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    List<Appointment> findByUserId(Long userId);

    Page<Appointment> findAllByUserId(Long userId, Pageable pageable);

    Page<Appointment> findAllByStaffId(Long staffId, Pageable pageable);

    boolean existsByServiceId(Long serviceId);
}