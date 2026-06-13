package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.message.MessageResponseDTO;
import com.yoot.booking.api.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel =
                MappingConstants
                        .ComponentModel
                        .SPRING
)
public interface MessageMapper {

    @Mapping(
            target = "conversationId",
            source = "conversation.id"
    )

    @Mapping(
            target = "senderId",
            source = "sender.id"
    )

    @Mapping( target = "senderEmail",
            source = "sender.email" )

    @Mapping(
            target = "senderName",
            expression =
                    "java(getDisplayName(entity))"
    )

    @Mapping(
            target = "senderAvatar",
            source = "sender.avatar"
    )

    MessageResponseDTO toDTO(
            Message entity
    );

    // ================= DISPLAY NAME =================
    default String getDisplayName(
            Message message
    ) {

        if (
                message.getSender()
                        == null
        ) {
            return "Unknown User";
        }

        String fullName =
                message
                        .getSender()
                        .getFullName();

        if (
                fullName != null &&
                        !fullName.isBlank()
        ) {
            return fullName;
        }

        return message
                .getSender()
                .getEmail();
    }
}