package com.yoot.booking.api.service;

import com.yoot.booking.api.entity.MediaType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String upload(
            MultipartFile file,
            String folder,
            MediaType type
    );

    void delete(
            String url,
            MediaType type
    );
}