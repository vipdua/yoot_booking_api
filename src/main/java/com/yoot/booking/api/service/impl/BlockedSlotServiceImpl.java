package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.blockedslot.*;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.BlockedSlotMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.*;
import com.yoot.booking.api.service.BlockedSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockedSlotServiceImpl implements BlockedSlotService {

    private final BlockedSlotRepository blockedSlotRepository;
    private final StaffRepository staffRepository;
    private final ScheduleRepository scheduleRepository;
    private final BlockedSlotMapper mapper;
    private final PaginationMapper paginationMapper;

    // ================= CREATE =================
    @Override
    @Transactional
    public ResultDTO<BlockedSlotResponseDTO> create(BlockedSlotCreateDTO dto) {

        Staff staff = staffRepository.findByIdAndIsActiveTrue(dto.staffId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff", dto.staffId()));

        // check schedule (block phải nằm trong giờ làm việc)
        boolean validSchedule = scheduleRepository
                .existsByStaffIdAndWorkDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        dto.staffId(),
                        dto.startTime().toLocalDate(),
                        dto.startTime().toLocalTime(),
                        dto.endTime().toLocalTime()
                );

        if (!validSchedule) {
            throw new IllegalArgumentException("Slot không thuộc lịch làm việc");
        }

        // check overlap block
        boolean overlap = blockedSlotRepository
                .existsByStaffIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        dto.staffId(),
                        dto.endTime(),
                        dto.startTime()
                );

        if (overlap) {
            throw new IllegalStateException("Slot đã bị khóa trước đó");
        }

        BlockedSlot entity = BlockedSlot.builder()
                .staff(staff)
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .reason(dto.reason())
                .build();

        return ResultDTO.success(
                mapper.toDTO(blockedSlotRepository.save(entity)),
                "Khóa slot thành công"
        );
    }

    // ================= GET ALL =================
    @Override
    public ResultListDTO<BlockedSlotResponseDTO> getAll(PagingRequestDTO request) {

        Page<BlockedSlot> page = blockedSlotRepository.findAll(request.toPageable());

        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách slot bị khóa thành công", pagination);
    }

    // ================= DELETE =================
    @Override
    @Transactional
    public ResultNoDataDTO delete(Long id) {

        BlockedSlot entity = blockedSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlockedSlot", id));

        blockedSlotRepository.delete(entity);

        return ResultNoDataDTO.success("Xóa block thành công");
    }
}