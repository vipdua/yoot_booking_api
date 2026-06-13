package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.message.ConversationResponseDTO;
import com.yoot.booking.api.dto.message.MessageResponseDTO;
import com.yoot.booking.api.dto.message.MessageSendDTO;
import com.yoot.booking.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // ================= SEND =================
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<MessageResponseDTO>
    sendMessage(
            @RequestBody
            MessageSendDTO dto
    ) {

        String email =

                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return messageService
                .sendMessage(
                        dto,
                        email
                );
    }

    // ================= GET MESSAGES =================
    @GetMapping("/conversation/{conversationId}")
    @PreAuthorize("isAuthenticated()")
    public ResultListDTO<MessageResponseDTO>
    getMessages(

            @PathVariable
            Long conversationId,

            @ModelAttribute
            PagingRequestDTO request
    ) {

        return messageService
                .getMessages(
                        conversationId,
                        request
                );
    }

    // ================= MY CONVERSATIONS =================
    @GetMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResultListDTO<ConversationResponseDTO>
    getMyConversations() {

        return messageService
                .getMyConversations();
    }

    // ================= CREATE CONVERSATION =================
    @PostMapping("/conversations")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<ConversationResponseDTO>
    createConversation() {

        return messageService
                .createConversation();
    }
}