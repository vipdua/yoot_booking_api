package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.exception.ResourceNotFoundException;
import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.entity.BookingService;
import com.yoot.booking.api.entity.MediaType;
import com.yoot.booking.api.mapper.BookingServiceMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.AppointmentRepository;
import com.yoot.booking.api.repository.BookingServiceRepository;
import com.yoot.booking.api.repository.ServiceCategoryRepository;
import com.yoot.booking.api.service.BookingServiceService;
import com.yoot.booking.api.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import com.yoot.booking.api.common.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceServiceImpl implements BookingServiceService {

    private final BookingServiceRepository repository;
    private final BookingServiceMapper mapper;
    private final PaginationMapper paginationMapper;
    private final ServiceCategoryRepository categoryRepository;
    private final AppointmentRepository appointmentRepository;
    private final FileStorageService fileStorageService;

    // ================= GET ALL =================
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

    // ================= GET BY ID =================
    @Override
    public ResultDTO<BookingServiceResponseDTO> getById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service", id));

        return ResultDTO.success(mapper.toDTO(entity), "Lấy dịch vụ thành công");
    }

    // ================= CREATE =================
    @Override
    public ResultDTO<BookingServiceResponseDTO> create(BookingServiceCreateDTO request) {

        var category = categoryRepository
                .findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", request.categoryId()));

        String imageUrl = null;

        // upload image
        if (request.imageFile() != null &&
                !request.imageFile().isEmpty()) {

            imageUrl = fileStorageService.upload(request.imageFile(), "service", MediaType.IMAGE);
        }

            BookingService entity = BookingService.builder()
                .name(request.name())
                .description(request.description())
                .duration(request.duration())
                .price(request.price())
                .imageUrl(imageUrl)
                .category(category)
                .build();

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Tạo dịch vụ thành công");
    }

    // ================= UPDATE =================
    @Override
    public ResultDTO<BookingServiceResponseDTO> update(Long id, BookingServiceUpdateDTO request) {

        BookingService entity = repository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Service", id));

        // update text field
        if (request.name() != null) {
            entity.setName(request.name());
        }

        if (request.description() != null) {
            entity.setDescription(request.description());
        }

        if (request.duration() != null) {
            entity.setDuration(request.duration());
        }

        if (request.price() != null) {
            entity.setPrice(request.price());
        }

        if (request.isActive() != null) {
            entity.setIsActive(request.isActive());
        }

        // update category
        if (request.categoryId() != null) {

            var category = categoryRepository
                    .findById(request.categoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category", request.categoryId()));

            entity.setCategory(category);
        }

        // replace image
        if (request.imageFile() != null &&
                !request.imageFile().isEmpty()) {

            // delete old image
            if (entity.getImageUrl() != null) {
                fileStorageService.delete(entity.getImageUrl(), MediaType.IMAGE);
            }

            // upload new image
            String imageUrl = fileStorageService.upload(request.imageFile(), "service", MediaType.IMAGE);

            entity.setImageUrl(imageUrl);
        }

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Cập nhật dịch vụ thành công");
    }

    // ================= DELETE =================
    @Override
    public ResultNoDataDTO delete(Long id) {

        BookingService entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service",
                                id
                        ));

        // check appointment using service
        if (appointmentRepository.existsByServiceId(id)) {

            throw new BadRequestException(
                    "Service đang được sử dụng"
            );
        }

        // delete image
        if (entity.getImageUrl() != null) {
            fileStorageService.delete(
                    entity.getImageUrl(),
                    MediaType.IMAGE
            );
        }

        repository.delete(entity);

        return ResultNoDataDTO.success("Xóa dịch vụ thành công");
    }
}