package com.yoot.booking.api.dto.message;

import com.yoot.booking.api.entity.MessageType;

import java.time.Instant;

public record MessageResponseDTO(

        Long id,

        Long conversationId,

        Long senderId,

        String senderName,

        String senderEmail,

        String senderAvatar,

        String content,

        String fileUrl,

        MessageType type,

        Boolean isSeen,

        Instant createdAt
) {}