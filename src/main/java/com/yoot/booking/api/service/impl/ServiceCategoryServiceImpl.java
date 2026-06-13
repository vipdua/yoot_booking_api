package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.category.*;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.entity.ServiceCategory;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.mapper.ServiceCategoryMapper;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.common.exception.BadRequestException;
import com.yoot.booking.api.repository.ServiceCategoryRepository;
import com.yoot.booking.api.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository repository;
    private final ServiceCategoryMapper mapper;
    private final PaginationMapper paginationMapper;
    private final BookingServiceRepository bookingServiceRepository;

    // ================= GET ALL =================
    @Override
    public ResultListDTO<ServiceCategoryResponseDTO> getAll(PagingRequestDTO request) {

        var pageable = request.toPageable();

        var page = repository.findAll(pageable);

        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách category thành công", pagination);
    }

    // ================= GET BY ID =================
    @Override
    public ResultDTO<ServiceCategoryResponseDTO> getById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        return ResultDTO.success(mapper.toDTO(entity), "Lấy category thành công");
    }

    // ================= CREATE =================
    @Override
    public ResultDTO<ServiceCategoryResponseDTO> create(ServiceCategoryCreateDTO request) {

        // check duplicate
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw new RuntimeException("Category đã tồn tại");
        }

        ServiceCategory entity = mapper.toEntity(request);

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Tạo category thành công");
    }

    // ================= UPDATE =================
    @Override
    public ResultDTO<ServiceCategoryResponseDTO> update(Long id, ServiceCategoryUpdateDTO request) {

        ServiceCategory entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        // ================= CHECK DUPLICATE =================
        if (repository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new RuntimeException("Category đã tồn tại");
        }

        mapper.update(entity, request);

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Cập nhật category thành công");
    }

    // ================= DELETE =================
    @Override
    public ResultNoDataDTO delete(Long id) {

        // check tồn tại
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }

        // check đang sử dụng
        if (bookingServiceRepository.existsByCategoryId(id)) {
            throw new BadRequestException("Category đang được sử dụng");
        }

        repository.deleteById(id);

        return ResultNoDataDTO.success("Xóa category thành công");
    }
}