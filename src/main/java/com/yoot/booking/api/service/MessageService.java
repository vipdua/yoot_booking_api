package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.message.ConversationResponseDTO;
import com.yoot.booking.api.dto.message.MessageResponseDTO;
import com.yoot.booking.api.dto.message.MessageSendDTO;

public interface MessageService {

    // ================= SEND =================
    ResultDTO<MessageResponseDTO>
    sendMessage( MessageSendDTO dto, String email );

    // ================= GET MESSAGES =================
    ResultListDTO<MessageResponseDTO>
    getMessages(
            Long conversationId,
            PagingRequestDTO request
    );

    // ================= MY CONVERSATIONS =================
    ResultListDTO<ConversationResponseDTO>
    getMyConversations();

    // ================= CREATE CONVERSATION =================
    ResultDTO<ConversationResponseDTO>
    createConversation();
}