package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.notification.NotificationResponseDTO;
import com.yoot.booking.api.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // ================= GET MY NOTIFICATIONS =================
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResultListDTO<NotificationResponseDTO> getMyNotifications(@ModelAttribute PagingRequestDTO request) {
        return notificationService.getMyNotifications(request);
    }

    // ================= MARK AS READ =================
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    // ================= UNREAD COUNT =================
    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<Integer> getUnreadCount() {
        return notificationService.getUnreadCount();
    }
}