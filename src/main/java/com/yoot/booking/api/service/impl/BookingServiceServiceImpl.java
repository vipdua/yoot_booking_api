package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.entity.BookingService;
import com.yoot.booking.api.mapper.BookingServiceMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.repository.ServiceCategoryRepository;
import com.yoot.booking.api.service.BookingServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceServiceImpl implements BookingServiceService {

    private final BookingServiceRepository repository;
    private final BookingServiceMapper mapper;
    private final PaginationMapper paginationMapper;
    private final ServiceCategoryRepository categoryRepository;

    @Override
    public ResultListDTO<BookingServiceResponseDTO> getAll(PagingRequestDTO request) {

        var pageable = request.toPageable();

        var page = repository.findAll(pageable);

        var data = page.getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();

        var pagination = paginationMapper.toPagination(page);

        return ResultListDTO.success(data, "Lấy danh sách dịch vụ thành công", pagination);
    }

    @Override
    public ResultDTO<BookingServiceResponseDTO> getById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        return ResultDTO.success(mapper.toDTO(entity), "Lấy dịch vụ thành công");
    }

    @Override
    public ResultDTO<BookingServiceResponseDTO> create(BookingServiceCreateDTO request) {

        var category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

        BookingService entity = mapper.toEntity(request);
        entity.setCategory(category);

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Tạo dịch vụ thành công");
    }

    @Override
    public ResultDTO<BookingServiceResponseDTO> update(Long id, BookingServiceUpdateDTO request) {

        BookingService entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        mapper.updateEntityFromDTO(request, entity);

        if (request.categoryId() != null) {
            var category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));

            entity.setCategory(category);
        }

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Cập nhật dịch vụ thành công");
    }

    @Override
    public ResultNoDataDTO delete(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Service", id);
        }

        repository.deleteById(id);

        return ResultNoDataDTO.success("Xóa dịch vụ thành công");
    }
}