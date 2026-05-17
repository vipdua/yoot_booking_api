package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.staff.*;
import com.yoot.booking.api.entity.BookingService;
import com.yoot.booking.api.entity.MediaType;
import com.yoot.booking.api.entity.Staff;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.StaffMapper;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.repository.StaffRepository;
import com.yoot.booking.api.service.FileStorageService;
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

    private final FileStorageService fileStorageService;

    // ================= GET ALL =================
    @Override
    public ResultListDTO<StaffResponseDTO> getAll(PagingRequestDTO request, Long serviceId) {

        var pageable = request.toPageable();

        Page<Staff> page;

        // filter theo service
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

    // ================= GET BY ID =================
    @Override
    public ResultDTO<StaffResponseDTO> getById(Long id) {

        Staff staff = staffRepository
                .findByIdAndIsActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Staff", id));

        return ResultDTO.success(staffMapper.toDTO(staff), "Lấy thông tin nhân viên thành công");
    }

    // ================= GET AVAILABLE =================
    @Override
    public ResultListDTO<StaffResponseDTO> getAvailable(Long serviceId, LocalDateTime start, LocalDateTime end, PagingRequestDTO request) {
        // validate
        if (serviceId == null) {
            throw new IllegalArgumentException("serviceId không được để trống");
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("start và end không được để trống");
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("end phải sau start");
        }

        // check service tồn tại
        bookingServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service", serviceId));

        var pageable = request.toPageable();

        Page<Staff> page = staffRepository.findAvailableStaffByService(
                        serviceId,
                        start,
                        end,
                        pageable
        );

        var data = page.getContent()
                .stream()
                .map(staffMapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách nhân viên rảnh thành công", pagination);
    }

    // ================= CREATE =================
    @Override
    @Transactional
    public ResultDTO<StaffResponseDTO> create(
            StaffCreateDTO dto
    ) {

        Staff staff = staffMapper.toEntity(dto);

        // ================= UPLOAD AVATAR =================
        if (
                dto.avatar() != null &&
                        !dto.avatar().isEmpty()
        ) {

            String avatarUrl =
                    fileStorageService.upload(
                            dto.avatar(),
                            "staff",
                            MediaType.IMAGE
                    );

            staff.setAvatarUrl(avatarUrl);
        }

        // ================= SERVICES =================
        if (
                dto.serviceIds() != null &&
                        !dto.serviceIds().isEmpty()
        ) {

            List<BookingService> services =
                    bookingServiceRepository.findAllById(
                            dto.serviceIds()
                    );

            // validate
            if (
                    services.size() !=
                            dto.serviceIds().size()
            ) {

                throw new ResourceNotFoundException(
                        "Một hoặc nhiều service không tồn tại"
                );
            }

            staff.setServices(services);
        }

        var saved = staffRepository.save(staff);

        return ResultDTO.success(
                staffMapper.toDTO(saved),
                "Tạo nhân viên thành công"
        );
    }

    // ================= UPDATE =================
    @Override
    @Transactional
    public ResultDTO<StaffResponseDTO> update(Long id, StaffUpdateDTO dto) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));

        // ================= UPDATE FIELD =================
        staffMapper.updateEntityFromDTO(dto, staff);

        // ================= UPDATE AVATAR =================
        if (dto.avatar() != null && !dto.avatar().isEmpty()) {
            // xóa avatar cũ
            if (staff.getAvatarUrl() != null && !staff.getAvatarUrl().isBlank()) {
                fileStorageService.delete(staff.getAvatarUrl(), MediaType.IMAGE);
            }

            // upload avatar mới
            String avatarUrl = fileStorageService.upload(dto.avatar(), "staff", MediaType.IMAGE);

            staff.setAvatarUrl(avatarUrl);
        }

        // ================= UPDATE SERVICES =================
        if (dto.serviceIds() != null) {

            List<BookingService> services =
                    bookingServiceRepository.findAllById(dto.serviceIds());

            // validate
            if (services.size() != dto.serviceIds().size()) {
                throw new ResourceNotFoundException("Một hoặc nhiều service không tồn tại");
            }

            staff.setServices(services);
        }

        var saved = staffRepository.save(staff);

        return ResultDTO.success(staffMapper.toDTO(saved), "Cập nhật nhân viên thành công");
    }

    // ================= DELETE =================
    @Override
    @Transactional
    public ResultNoDataDTO delete(Long id) {

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", id));

        // soft delete
        if (!staff.getIsActive()) {
            throw new IllegalStateException("Staff đã bị xóa trước đó");
        }

        staff.setIsActive(false);

        staffRepository.save(staff);

        return ResultNoDataDTO.success("Xóa nhân viên thành công");
    }
}