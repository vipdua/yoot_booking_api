package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.message.*;
import com.yoot.booking.api.entity.*;
import com.yoot.booking.api.mapper.ConversationMapper;
import com.yoot.booking.api.mapper.MessageMapper;
import com.yoot.booking.api.mapper.PaginationMapper;
import com.yoot.booking.api.repository.ConversationRepository;
import com.yoot.booking.api.repository.MessageRepository;
import com.yoot.booking.api.repository.UserRepository;
import com.yoot.booking.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository
            messageRepository;

    private final ConversationRepository
            conversationRepository;

    private final UserRepository
            userRepository;

    private final MessageMapper
            messageMapper;

    private final ConversationMapper
            conversationMapper;

    private final PaginationMapper
            paginationMapper;

    // ================= CURRENT USER =================
    private User getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User không tồn tại"
                        ));
    }

    // ================= SEND MESSAGE =================
    @Override
    public ResultDTO<MessageResponseDTO>
    sendMessage(

            MessageSendDTO dto,

            String email
    ) {

        User sender =

                userRepository
                        .findByEmail(email)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "User không tồn tại"
                                )
                        );

        Conversation conversation =
                conversationRepository
                        .findById(
                                dto.conversationId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Conversation không tồn tại"
                                ));

        Message message =
                Message.builder()

                        .conversation(
                                conversation
                        )

                        .sender(sender)

                        .content(
                                dto.content()
                        )

                        .type(
                                dto.type()
                        )

                        .isSeen(false)

                        .build();

        Message saved =
                messageRepository
                        .save(message);

        // update conversation time
        conversation.setUpdatedAt(
                Instant.now()
        );

        conversationRepository
                .save(conversation);

        return ResultDTO.success(

                messageMapper.toDTO(saved),

                "Gửi tin nhắn thành công"
        );
    }

    // ================= GET MESSAGES =================
    @Override
    public ResultListDTO<MessageResponseDTO>
    getMessages(
            Long conversationId,
            PagingRequestDTO request
    ) {

        Pageable pageable =
                request.toPageable();

        Page<Message> page =
                messageRepository
                        .findAllByConversationIdOrderByCreatedAtAsc(
                                conversationId,
                                pageable
                        );

        List<MessageResponseDTO> data =
                page.getContent()
                        .stream()
                        .map(messageMapper::toDTO)
                        .toList();

        return ResultListDTO.success(

                data,

                "Lấy danh sách tin nhắn thành công",

                paginationMapper.toPagination(
                        page
                )
        );
    }

    // ================= MY CONVERSATIONS =================
    @Override
    public ResultListDTO<ConversationResponseDTO>
    getMyConversations() {

        User currentUser =
                getCurrentUser();

        List<Conversation> conversations;

        // ================= STAFF =================
        if (
                currentUser.getRole()
                        == Role.STAFF
        ) {

            conversations =
                    conversationRepository
                            .findAllBySupportStaffIdOrderByUpdatedAtDesc(
                                    currentUser.getId()
                            );

        } else {

            // ================= CUSTOMER =================
            conversations =
                    conversationRepository
                            .findAllByCustomerIdOrderByUpdatedAtDesc(
                                    currentUser.getId()
                            );
        }

        List<ConversationResponseDTO> data =

                conversations
                        .stream()

                        .map(conversation -> {

                            ConversationResponseDTO dto =

                                    conversationMapper
                                            .toDTO(
                                                    conversation
                                            );

                            Message latestMessage =

                                    messageRepository
                                            .findTopByConversationIdOrderByCreatedAtDesc(
                                                    conversation.getId()
                                            )

                                            .orElse(null);

                            if (latestMessage == null) {
                                return dto;
                            }

                            return new ConversationResponseDTO(

                                    dto.id(),

                                    // ================= CUSTOMER =================
                                    dto.customerId(),
                                    dto.customerName(),
                                    dto.customerAvatar(),

                                    // ================= SUPPORT STAFF =================
                                    dto.supportStaffId(),
                                    dto.supportStaffName(),
                                    dto.supportStaffAvatar(),

                                    // ================= LAST MESSAGE =================
                                    latestMessage.getContent(),

                                    latestMessage.getCreatedAt(),

                                    0
                            );
                        })

                        .toList();

        return ResultListDTO.success(

                data,

                "Lấy danh sách conversation thành công"
        );
    }

    // ================= CREATE CONVERSATION =================
    @Override
    public ResultDTO<ConversationResponseDTO> createConversation() {

        User currentUser = getCurrentUser();

        // ================= FIND SUPPORT STAFF =================
        User supportStaff =
                userRepository
                        .findFirstByRoleAndIdNot(

                                Role.STAFF,

                                currentUser.getId()
                        )

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Không có nhân viên hỗ trợ"
                                )
                        );

        // ================= EXISTED =================
        var existed =
                conversationRepository
                        .findByCustomerIdAndSupportStaffId(

                                currentUser.getId(),

                                supportStaff.getId()
                        );

        if (existed.isPresent()) {

            return ResultDTO.success(

                    conversationMapper.toDTO(
                            existed.get()
                    ),

                    "Conversation đã tồn tại"
            );
        }

        // ================= CREATE =================
        Conversation conversation =
                Conversation.builder()

                        .customer(
                                currentUser
                        )

                        .supportStaff(
                                supportStaff
                        )

                        .isActive(true)

                        .build();

        Conversation saved =
                conversationRepository
                        .save(conversation);

        return ResultDTO.success(

                conversationMapper.toDTO(
                        saved
                ),

                "Tạo conversation thành công"
        );
    }
}