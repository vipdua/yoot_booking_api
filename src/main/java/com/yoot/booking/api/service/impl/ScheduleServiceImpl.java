package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.schedule.*;
import com.yoot.booking.api.entity.Schedule;
import com.yoot.booking.api.entity.Staff;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.ScheduleMapper;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.repository.ScheduleRepository;
import com.yoot.booking.api.repository.StaffRepository;
import com.yoot.booking.api.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;
    private final ScheduleMapper scheduleMapper;
    private final PaginationMapper paginationMapper;

    @Override
    public ResultListDTO<ScheduleResponseDTO> getAll(PagingRequestDTO request, Long staffId, LocalDate date) {

        var pageable = request.toPageable();

        Page<Schedule> page;

        if (staffId != null && date != null) {
            page = scheduleRepository.findByStaffIdAndWorkDate(staffId, date, pageable);
        } else if (staffId != null) {
            page = scheduleRepository.findByStaffId(staffId, pageable);
        } else if (date != null) {
            page = scheduleRepository.findByWorkDate(date, pageable);
        } else {
            page = scheduleRepository.findAll(pageable);
        }

        var data = page.getContent()
                .stream()
                .map(scheduleMapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách lịch làm việc thành công", pagination);
    }

    @Override
    public ResultDTO<ScheduleResponseDTO> getById(Long id) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", id));

        return ResultDTO.success(scheduleMapper.toDTO(schedule), "Lấy lịch làm việc thành công");
    }

    @Override
    @Transactional
    public ResultDTO<ScheduleResponseDTO> create(ScheduleCreateDTO dto) {

        boolean overlap = scheduleRepository
                        .existsByStaffIdAndWorkDateAndStartTimeLessThanAndEndTimeGreaterThan(
                                dto.staffId(),
                                dto.workDate(),
                                dto.endTime(),
                                dto.startTime()
                        );

        if (overlap) {
            throw new RuntimeException("Lịch làm việc bị trùng thời gian");
        }

        if (dto.workDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Không thể tạo lịch quá khứ");
        }

        if (dto.startTime().isAfter(dto.endTime())) {
            throw new RuntimeException("Start time phải trước end time");
        }

        Staff staff = staffRepository.findByIdAndIsActiveTrue(dto.staffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", dto.staffId()));

        Schedule schedule = scheduleMapper.toEntity(dto);
        schedule.setStaff(staff);

        var saved = scheduleRepository.save(schedule);

        return ResultDTO.success(scheduleMapper.toDTO(saved), "Tạo lịch làm việc thành công");
    }

    @Override
    @Transactional
    public ResultDTO<ScheduleResponseDTO> update(Long id, ScheduleUpdateDTO dto) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", id));

        LocalTime startTime = dto.startTime() != null
                        ? dto.startTime()
                        : schedule.getStartTime();

        LocalTime endTime = dto.endTime() != null
                        ? dto.endTime()
                        : schedule.getEndTime();

        Long staffId = dto.staffId() != null
                        ? dto.staffId()
                        : schedule.getStaff().getId();

        LocalDate workDate = dto.workDate() != null
                        ? dto.workDate()
                        : schedule.getWorkDate();

        // ================= VALIDATE TIME =================
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time phải trước end time");
        }

        // ================= CHECK OVERLAP =================
        boolean overlap = scheduleRepository
                        .existsByStaffIdAndWorkDateAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                                staffId,
                                workDate,
                                endTime,
                                startTime,
                                id
                        );

        if (overlap) {
            throw new RuntimeException("Lịch làm việc bị trùng thời gian");
        }

        // ================= UPDATE STAFF =================
        if (dto.staffId() != null) {

            Staff staff = staffRepository
                    .findByIdAndIsActiveTrue(dto.staffId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Staff", dto.staffId()));

            schedule.setStaff(staff);
        }

        // ================= UPDATE FIELD =================
        scheduleMapper.updateEntityFromDTO(dto, schedule);

        var saved = scheduleRepository.save(schedule);

        return ResultDTO.success(scheduleMapper.toDTO(saved), "Cập nhật lịch làm việc thành công");
    }

    @Override
    @Transactional
    public ResultNoDataDTO delete(Long id) {

        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule", id);
        }

        scheduleRepository.deleteById(id);

        return ResultNoDataDTO.success("Xóa lịch làm việc thành công");
    }
}