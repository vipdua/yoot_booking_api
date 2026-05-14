package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.message.MessageResponseDTO;
import com.yoot.booking.api.dto.message.MessageSendDTO;
import com.yoot.booking.api.dto.message.SocketMessageDTO;
import com.yoot.booking.api.security.JwtUtil;
import com.yoot.booking.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService
            messageService;

    private final SimpMessagingTemplate
            messagingTemplate;

    private final JwtUtil jwtUtil;

    // ================= SEND MESSAGE =================
    @MessageMapping("/chat.send")
    public void sendMessage(
            SocketMessageDTO dto
    ) {

        String email =
                jwtUtil.extractEmail(
                        dto.token()
                );

        ResultDTO<MessageResponseDTO>
                result =

                messageService.sendMessage(

                        new MessageSendDTO(

                                dto.conversationId(),

                                dto.content(),

                                dto.type()
                        ),

                        email
                );

        MessageResponseDTO message =
                result.getData();

        messagingTemplate.convertAndSend(

                "/topic/conversation/"
                        + dto.conversationId(),

                message
        );
    }
}