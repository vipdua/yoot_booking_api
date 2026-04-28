package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.staff.*;
import com.yoot.booking.api.entity.BookingService;
import com.yoot.booking.api.entity.Staff;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.StaffMapper;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.repository.StaffRepository;
import com.yoot.booking.api.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final BookingServiceRepository bookingServiceRepository;
    private final StaffMapper staffMapper;
    private final PaginationMapper paginationMapper;

    @Override
    public ResultListDTO<StaffResponseDTO> getAll(PagingRequestDTO request, Long serviceId) {

        var pageable = request.toPageable();

        Page<Staff> page;
        if (serviceId != null) {
            page = staffRepository.findByIsActiveAndServicesId(true, serviceId, pageable);
        } else {
            page = staffRepository.findByIsActive(true, pageable);
        }

        var data = page.getContent()
                .stream()
                .map(staffMapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách nhân viên thành công", pagination);
    }

    @Override
    public ResultDTO<StaffResponseDTO> getById(Long id) {

        Staff staff = staffRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));

        return ResultDTO.success(staffMapper.toDTO(staff), "Lấy thông tin nhân viên thành công");
    }

    @Override
    public ResultListDTO<StaffResponseDTO> getAvailable(LocalDateTime start, LocalDateTime end, PagingRequestDTO request) {

        if (start == null || end == null) {
            throw new IllegalArgumentException("Tham số 'start' và 'end' không được để trống");
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Thời gian 'end' phải sau 'start'");
        }

        var pageable = request.toPageable();

        Page<Staff> page = staffRepository.findAvailableStaff(start, end, pageable);

        var data = page.getContent()
                .stream()
                .map(staffMapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách nhân viên rảnh thành công", pagination);
    }

    @Override
    @Transactional
    public ResultDTO<StaffResponseDTO> create(StaffCreateDTO dto) {

        Staff staff = staffMapper.toEntity(dto);

        if (dto.serviceIds() != null && !dto.serviceIds().isEmpty()) {
            List<BookingService> services = bookingServiceRepository.findAllById(dto.serviceIds());

            if (services.size() != dto.serviceIds().size()) {
                throw new ResourceNotFoundException("Service không tồn tại");
            }

            staff.setServices(services);
        }

        var saved = staffRepository.save(staff);

        return ResultDTO.success(staffMapper.toDTO(saved), "Tạo nhân viên thành công");
    }

    @Override
    @Transactional
    public ResultDTO<StaffResponseDTO> update(Long id, StaffUpdateDTO dto) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));

        staffMapper.updateEntityFromDTO(dto, staff);

        if (dto.serviceIds() != null) {
            List<BookingService> services = bookingServiceRepository.findAllById(dto.serviceIds());

            if (services.size() != dto.serviceIds().size()) {
                throw new ResourceNotFoundException("Service không tồn tại");
            }

            staff.setServices(services);
        }

        var saved = staffRepository.save(staff);

        return ResultDTO.success(staffMapper.toDTO(saved), "Cập nhật nhân viên thành công");
    }

    @Override
    @Transactional
    public ResultNoDataDTO delete(Long id) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));

        if (!staff.getIsActive()) {
            throw new IllegalStateException("Staff đã bị xóa trước đó");
        }

        staff.setIsActive(false);
        staffRepository.save(staff);

        return ResultNoDataDTO.success("Xóa nhân viên thành công");
    }
}