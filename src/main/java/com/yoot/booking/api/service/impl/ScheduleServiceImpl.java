package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.schedule.*;
import com.yoot.booking.api.entity.Schedule;
import com.yoot.booking.api.entity.Staff;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.ScheduleMapper;
import com.yoot.booking.api.repository.ScheduleRepository;
import com.yoot.booking.api.repository.StaffRepository;
import com.yoot.booking.api.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

        scheduleMapper.updateEntityFromDTO(dto, schedule);

        if (dto.staffId() != null) {
            Staff staff = staffRepository.findByIdAndIsActiveTrue(dto.staffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff", dto.staffId()));
            schedule.setStaff(staff);
        }

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
