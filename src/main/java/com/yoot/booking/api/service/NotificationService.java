package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.notification.NotificationResponseDTO;
import com.yoot.booking.api.entity.NotificationType;

public interface NotificationService {

    void createNotification(Long userId, String title, String content, NotificationType type, String targetUrl);

    ResultListDTO<NotificationResponseDTO> getMyNotifications(PagingRequestDTO request);

    ResultDTO<NotificationResponseDTO> markAsRead(Long id);

    ResultDTO<Integer> getUnreadCount();
}