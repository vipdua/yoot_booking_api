package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByStaffId(Long staffId, Pageable pageable);

    Page<Schedule> findByWorkDate(LocalDate workDate, Pageable pageable);

    Page<Schedule> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate, Pageable pageable);
}
