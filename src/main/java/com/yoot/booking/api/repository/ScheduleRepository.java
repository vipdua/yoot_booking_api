package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByStaffId(Long staffId, Pageable pageable);

    Page<Schedule> findByWorkDate(LocalDate workDate, Pageable pageable);

    Page<Schedule> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate, Pageable pageable);

    // Dùng cho Slot generation — không phân trang
    List<Schedule> findByWorkDate(LocalDate workDate);

    List<Schedule> findByStaffIdAndWorkDate(Long staffId, LocalDate workDate);

    // Kiểm tra staff có lịch làm việc bao phủ khung giờ đặt không
    boolean existsByStaffIdAndWorkDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long staffId, LocalDate workDate, LocalTime startTime, LocalTime endTime);

    boolean existsByStaffIdAndWorkDateAndStartTimeLessThanAndEndTimeGreaterThan( Long staffId, LocalDate workDate, LocalTime endTime, LocalTime startTime );

    boolean existsByStaffIdAndWorkDateAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot( Long staffId, LocalDate workDate, LocalTime endTime, LocalTime startTime, Long id );
}
