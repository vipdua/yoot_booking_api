package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.slot.SlotResponseDTO;
import com.yoot.booking.api.entity.BlockedSlot;
import com.yoot.booking.api.entity.Schedule;
import com.yoot.booking.api.entity.Staff;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.BlockedSlotRepository;
import com.yoot.booking.api.repository.ScheduleRepository;
import com.yoot.booking.api.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final ScheduleRepository scheduleRepository;
    private final PaginationMapper paginationMapper;
    private final BlockedSlotRepository blockedSlotRepository;

    private static final int SLOT_DURATION_MINUTES = 30;

    @Override
    public ResultListDTO<SlotResponseDTO> getAvailableSlots(
            LocalDate date,
            Long staffId,
            Long serviceId,
            PagingRequestDTO request) {

        if (date == null) {
            throw new IllegalArgumentException("date là bắt buộc");
        }

        // ================= 1. Lấy schedule =================
        List<Schedule> schedules;

        if (staffId != null) {
            schedules = scheduleRepository.findByStaffIdAndWorkDate(staffId, date);
        } else {
            schedules = scheduleRepository.findByWorkDate(date);
        }

        // ================= 2. Filter theo service =================
        if (serviceId != null) {
            schedules = schedules.stream()
                    .filter(s -> hasService(s.getStaff(), serviceId))
                    .toList();
        }

        // ================= 3. Generate slot =================
        List<SlotResponseDTO> allSlots = new ArrayList<>();

        for (Schedule schedule : schedules) {

            // Lấy toàn bộ blocked slot 1 lần (OPTIMIZE)
            List<BlockedSlot> blockedSlots = blockedSlotRepository
                    .findByStaffIdAndStartTimeLessThanAndEndTimeGreaterThan(
                            schedule.getStaff().getId(),
                            LocalDateTime.of(schedule.getWorkDate(), schedule.getEndTime()),
                            LocalDateTime.of(schedule.getWorkDate(), schedule.getStartTime())
                    );

            LocalTime start = schedule.getStartTime();
            LocalTime end = schedule.getEndTime();

            while (start.plusMinutes(SLOT_DURATION_MINUTES).compareTo(end) <= 0) {

                LocalTime slotEnd = start.plusMinutes(SLOT_DURATION_MINUTES);

                LocalDateTime slotStartDateTime =
                        LocalDateTime.of(schedule.getWorkDate(), start);

                LocalDateTime slotEndDateTime =
                        LocalDateTime.of(schedule.getWorkDate(), slotEnd);

                // check blocked (IN-MEMORY)
                boolean isBlocked = blockedSlots.stream().anyMatch(b ->
                        b.getStartTime().isBefore(slotEndDateTime)
                                && b.getEndTime().isAfter(slotStartDateTime)
                );

                if (!isBlocked) {
                    allSlots.add(new SlotResponseDTO(
                            schedule.getWorkDate(),
                            start,
                            slotEnd,
                            schedule.getStaff().getId()
                    ));
                }

                start = slotEnd;
            }
        }

        // ================= 4. Pagination =================
        var pageable = request.toPageable();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), allSlots.size());

        List<SlotResponseDTO> pagedSlots =
                (startIndex >= allSlots.size()) ? List.of() : allSlots.subList(startIndex, endIndex);

        PaginationDTO pagination = new PaginationDTO(
                pageable.getPageNumber() + 1,
                (int) Math.ceil((double) allSlots.size() / pageable.getPageSize()),
                pageable.getPageSize(),
                allSlots.size()
        );

        return ResultListDTO.success(pagedSlots, "Lấy danh sách slot trống thành công", pagination);
    }

    // ================= Helper =================
    private boolean hasService(Staff staff, Long serviceId) {
        return staff.getServices().stream()
                .anyMatch(s -> s.getId().equals(serviceId));
    }
}