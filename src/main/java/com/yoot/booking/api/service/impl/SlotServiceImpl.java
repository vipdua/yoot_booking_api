package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.slot.SlotResponseDTO;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.AppointmentRepository;
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

    private final AppointmentRepository appointmentRepository;

    private static final int SLOT_DURATION_MINUTES = 30;

    @Override
    public ResultListDTO<SlotResponseDTO> getAvailableSlots(LocalDate date, Long staffId, Long serviceId, PagingRequestDTO request) {

        if (date == null) {
            throw new IllegalArgumentException("date là bắt buộc");
        }

        // ================= GET SCHEDULE =================
        List<Schedule> schedules;
        if (staffId != null) {
            schedules = scheduleRepository.findByStaffIdAndWorkDate(staffId, date);
        } else {
            schedules = scheduleRepository.findByWorkDate(date);
        }

        // ================= FILTER SERVICE =================
        if (serviceId != null) {
            schedules = schedules.stream().filter(s -> hasService(s.getStaff(), serviceId)).toList();
        }

        // ================= GENERATE SLOT =================
        List<SlotResponseDTO> allSlots = new ArrayList<>();

        for (Schedule schedule : schedules) {

            // ================= BLOCKED SLOT =================
            List<BlockedSlot> blockedSlots =
                    blockedSlotRepository
                            .findByStaffIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                    schedule.getStaff().getId(),
                                    LocalDateTime.of(schedule.getWorkDate(), schedule.getEndTime()),
                                    LocalDateTime.of(schedule.getWorkDate(), schedule.getStartTime())
                            );

            // ================= APPOINTMENTS =================
            List<Appointment> appointments =
                    appointmentRepository
                            .findByStaffIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(

                                    schedule.getStaff().getId(),

                                    List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED),

                                    LocalDateTime.of(schedule.getWorkDate(), schedule.getEndTime()),

                                    LocalDateTime.of(schedule.getWorkDate(), schedule.getStartTime()));

            LocalTime start = schedule.getStartTime();

            LocalTime end = schedule.getEndTime();

            while (start.plusMinutes(SLOT_DURATION_MINUTES).compareTo(end) <= 0) {
                LocalTime slotEnd = start.plusMinutes(SLOT_DURATION_MINUTES);

                LocalDateTime slotStartDateTime = LocalDateTime.of(schedule.getWorkDate(), start);

                LocalDateTime slotEndDateTime = LocalDateTime.of(schedule.getWorkDate(), slotEnd);

                // ================= SKIP PAST SLOT =================
                if (slotStartDateTime.isBefore(LocalDateTime.now())) {
                    start = slotEnd;
                    continue;
                }

                // ================= CHECK BLOCKED =================
                boolean isBlocked = blockedSlots.stream().anyMatch(b ->
                                b.getStartTime().isBefore(slotEndDateTime) && b.getEndTime().isAfter(slotStartDateTime));

                // ================= CHECK BOOKED =================
                boolean isBooked =
                        appointments.stream().anyMatch(a ->

                                a.getStartTime().isBefore(slotEndDateTime) && a.getEndTime().isAfter(slotStartDateTime));

                // ================= AVAILABLE =================
                if (!isBlocked && !isBooked) {
                    allSlots.add(new SlotResponseDTO(
                                    schedule.getWorkDate(),
                                    start,
                                    slotEnd,
                                    schedule.getStaff().getId()));
                }
                start = slotEnd;
            }
        }

        // ================= PAGINATION =================
        var pageable = request.toPageable();

        int startIndex = (int) pageable.getOffset();

        int endIndex = Math.min(startIndex + pageable.getPageSize(), allSlots.size());

        List<SlotResponseDTO> pagedSlots =
                (startIndex >= allSlots.size()) ? List.of() : allSlots.subList(startIndex, endIndex);

        PaginationDTO pagination =
                new PaginationDTO(pageable.getPageNumber() + 1,
                        (int) Math.ceil((double) allSlots.size() / pageable.getPageSize()),
                        pageable.getPageSize(),
                        allSlots.size());

        return ResultListDTO.success(pagedSlots, "Lấy danh sách slot trống thành công", pagination);
    }

    // ================= HELPER =================
    private boolean hasService(Staff staff, Long serviceId) {
        return staff.getServices().stream().anyMatch(s -> s.getId().equals(serviceId));
    }
}