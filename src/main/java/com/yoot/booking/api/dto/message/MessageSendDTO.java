package com.yoot.booking.api.dto.message;

import com.yoot.booking.api.entity.MessageType;

public record MessageSendDTO(

        Long conversationId,

        String content,

        MessageType type
) {}