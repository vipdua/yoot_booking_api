package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.banner.*;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.BannerMapper;
import com.yoot.booking.api.repository.BannerRepository;
import com.yoot.booking.api.service.BannerService;
import com.yoot.booking.api.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository repository;
    private final BannerMapper mapper;
    private final FileStorageService fileStorageService;

    // ================= GET ACTIVE =================
    @Override
    public ResultListDTO<BannerResponseDTO> getByPosition(
            BannerPosition position
    ) {

        LocalDateTime now = LocalDateTime.now();

        var data = repository
                .findByPositionAndIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByOrderIndexAsc(
                        position,
                        now,
                        now
                )
                .stream()
                .map(mapper::toDTO)
                .toList();

        return ResultListDTO.success(
                data,
                "Lấy danh sách banner thành công",
                null
        );
    }

    // ================= CREATE =================
    @Override
    public ResultDTO<BannerResponseDTO> create(
            BannerCreateDTO dto
    ) {

        validateBannerMedia(dto.imageFile(), dto.videoFile());

        String imageUrl = null;
        String videoUrl = null;

        // upload image
        if (dto.imageFile() != null &&
                !dto.imageFile().isEmpty()) {

            imageUrl = fileStorageService.upload(
                    dto.imageFile(),
                    "banner",
                    MediaType.IMAGE
            );
        }

        // upload video
        if (dto.videoFile() != null &&
                !dto.videoFile().isEmpty()) {

            videoUrl = fileStorageService.upload(
                    dto.videoFile(),
                    "banner",
                    MediaType.VIDEO
            );
        }

        // auto order
        int maxOrder = repository
                .findByPositionOrderByOrderIndexAsc(dto.position())
                .stream()
                .mapToInt(Banner::getOrderIndex)
                .max()
                .orElse(0);

        Banner entity = Banner.builder()
                .title(dto.title())
                .description(dto.description())
                .imageUrl(imageUrl)
                .videoUrl(videoUrl)
                .ctaText(dto.ctaText())
                .ctaLink(dto.ctaLink())
                .position(dto.position())
                .isActive(dto.isActive())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .orderIndex(maxOrder + 1)
                .build();

        var saved = repository.save(entity);

        return ResultDTO.success(
                mapper.toDTO(saved),
                "Tạo banner thành công"
        );
    }

    // ================= UPDATE =================
    @Override
    public ResultDTO<BannerResponseDTO> update(
            Long id,
            BannerUpdateDTO dto
    ) {

        Banner entity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Banner không tồn tại"));

        // update basic field
        if (dto.title() != null) {
            entity.setTitle(dto.title());
        }

        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }

        if (dto.ctaText() != null) {
            entity.setCtaText(dto.ctaText());
        }

        if (dto.ctaLink() != null) {
            entity.setCtaLink(dto.ctaLink());
        }

        if (dto.position() != null) {
            entity.setPosition(dto.position());
        }

        if (dto.isActive() != null) {
            entity.setIsActive(dto.isActive());
        }

        if (dto.startDate() != null) {
            entity.setStartDate(dto.startDate());
        }

        if (dto.endDate() != null) {
            entity.setEndDate(dto.endDate());
        }

        // replace image
        if (dto.imageFile() != null &&
                !dto.imageFile().isEmpty()) {

            // delete old
            if (entity.getImageUrl() != null) {
                fileStorageService.delete(
                        entity.getImageUrl(),
                        MediaType.IMAGE
                );
            }

            String imageUrl = fileStorageService.upload(
                    dto.imageFile(),
                    "banner",
                    MediaType.IMAGE
            );

            entity.setImageUrl(imageUrl);
        }

        // replace video
        if (dto.videoFile() != null &&
                !dto.videoFile().isEmpty()) {

            // delete old
            if (entity.getVideoUrl() != null) {
                fileStorageService.delete(
                        entity.getVideoUrl(),
                        MediaType.VIDEO
                );
            }

            String videoUrl = fileStorageService.upload(
                    dto.videoFile(),
                    "banner",
                    MediaType.VIDEO
            );

            entity.setVideoUrl(videoUrl);
        }

        var saved = repository.save(entity);

        return ResultDTO.success(
                mapper.toDTO(saved),
                "Cập nhật banner thành công"
        );
    }

    // ================= DELETE =================
    @Override
    public ResultNoDataDTO delete(Long id) {

        Banner entity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Banner không tồn tại"));

        // delete cloudinary image
        if (entity.getImageUrl() != null) {

            fileStorageService.delete(
                    entity.getImageUrl(),
                    MediaType.IMAGE
            );
        }

        // delete cloudinary video
        if (entity.getVideoUrl() != null) {

            fileStorageService.delete(
                    entity.getVideoUrl(),
                    MediaType.VIDEO
            );
        }

        repository.delete(entity);

        return ResultNoDataDTO.success(
                "Xóa banner thành công"
        );
    }

    // ================= VALIDATE =================
    private void validateBannerMedia(
            org.springframework.web.multipart.MultipartFile imageFile,
            org.springframework.web.multipart.MultipartFile videoFile
    ) {

        boolean noImage =
                imageFile == null || imageFile.isEmpty();

        boolean noVideo =
                videoFile == null || videoFile.isEmpty();

        if (noImage && noVideo) {
            throw new RuntimeException(
                    "Banner phải có image hoặc video"
            );
        }
    }
}