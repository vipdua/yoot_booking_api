package com.yoot.booking.api.dto.notification;

import com.yoot.booking.api.entity.NotificationType;

import java.time.Instant;

public record NotificationResponseDTO(
        Long id,
        Long userId,
        String title,
        String content,
        NotificationType type,
        Boolean isRead,
        String targetUrl,
        Instant createdAt
) {}