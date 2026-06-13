package com.yoot.booking.api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import com.yoot.booking.api.entity.MediaType;
import com.yoot.booking.api.service.FileStorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public String upload(
            MultipartFile file,
            String folder,
            MediaType type
    ) {

        validateFile(file, type);

        try {

            @SuppressWarnings("unchecked")
            Map<String, Object> result =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", folder,
                                    "resource_type", type.name().toLowerCase()
                            )
                    );

            return result.get("secure_url").toString();

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Upload file thất bại: " + e.getMessage()
            );
        }
    }

    @Override
    public void delete(String url, MediaType type) {

        try {

            String publicId = extractPublicId(url);

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type",
                            type.name().toLowerCase()
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException("Delete file thất bại");
        }
    }

    // ================= VALIDATE =================

    private void validateFile(
            MultipartFile file,
            MediaType type
    ) {

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new RuntimeException("File type không hợp lệ");
        }

        if (type == MediaType.IMAGE &&
                !contentType.startsWith("image/")) {
            throw new RuntimeException("File phải là image");
        }

        if (type == MediaType.VIDEO &&
                !contentType.startsWith("video/")) {
            throw new RuntimeException("File phải là video");
        }

        validateExtension(file, type);
    }

    private void validateExtension(
            MultipartFile file,
            MediaType type
    ) {

        String filename = file.getOriginalFilename();

        if (filename == null) {
            throw new RuntimeException("File name không hợp lệ");
        }

        String ext =
                filename.substring(filename.lastIndexOf(".") + 1)
                        .toLowerCase();

        if (type == MediaType.IMAGE &&
                !List.of("jpg", "jpeg", "png", "webp")
                        .contains(ext)) {

            throw new RuntimeException("Định dạng image không hợp lệ");
        }

        if (type == MediaType.VIDEO &&
                !List.of("mp4", "mov", "avi")
                        .contains(ext)) {

            throw new RuntimeException("Định dạng video không hợp lệ");
        }
    }

    private String extractPublicId(String url) {

        int dotIndex = url.lastIndexOf('.');

        return url.substring(
                url.indexOf("yoot"),
                dotIndex
        );
    }
}