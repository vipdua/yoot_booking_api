package com.yoot.booking.api.dto.message;

import java.time.Instant;

public record ConversationResponseDTO(

        Long id,

        // ================= CUSTOMER =================
        Long customerId,

        String customerName,

        String customerAvatar,

        // ================= SUPPORT STAFF =================
        Long supportStaffId,

        String supportStaffName,

        String supportStaffAvatar,

        // ================= LAST MESSAGE =================
        String lastMessage,

        Instant lastMessageTime,

        Integer unreadCount
) {}