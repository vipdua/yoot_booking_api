package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.notification.NotificationResponseDTO;
import com.yoot.booking.api.entity.Notification;
import com.yoot.booking.api.entity.NotificationType;
import com.yoot.booking.api.entity.User;
import com.yoot.booking.api.mapper.NotificationMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.NotificationRepository;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final PaginationMapper paginationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // ================= CURRENT USER =================
    private User getCurrentUser() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    // ================= CREATE =================
    @Override
    public void createNotification(Long userId, String title, String content, NotificationType type, String targetUrl) {

        User user = userRepository
                .findById(userId).orElseThrow(() -> new EntityNotFoundException("User không tồn tại"));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .targetUrl(targetUrl)
                .isRead(false)
                .createdAt(Instant.now())
                .build();

        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notificationMapper.toDTO(saved));
    }

    // ================= GET MY NOTIFICATIONS =================
    @Override
    public ResultListDTO<NotificationResponseDTO> getMyNotifications(PagingRequestDTO request) {

        User currentUser = getCurrentUser();

        Pageable pageable = request.toPageable();

        Page<Notification> page = notificationRepository
                .findAllByUserIdOrderByCreatedAtDesc(currentUser.getId(), pageable);

        var data = page.getContent()
                .stream()
                .map(notificationMapper::toDTO)
                .toList();

        return ResultListDTO.success(data, "Lấy danh sách notification thành công", paginationMapper.toPagination(page));
    }

    // ================= MARK AS READ =================
    @Override
    public ResultDTO<NotificationResponseDTO> markAsRead(Long id) {

        User currentUser = getCurrentUser();

        Notification notification = notificationRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification không tồn tại"));

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Không có quyền");
        }

        notification.setIsRead(true);

        Notification saved = notificationRepository.save(notification);

        return ResultDTO.success(notificationMapper.toDTO(saved), "Đã đọc notification");
    }

    // ================= UNREAD COUNT =================
    @Override
    public ResultDTO<Integer> getUnreadCount() {
        User currentUser = getCurrentUser();

        Integer count = notificationRepository.countByUserIdAndIsReadFalse(currentUser.getId());

        return ResultDTO.success(count, "Lấy số notification chưa đọc thành công");
    }
}