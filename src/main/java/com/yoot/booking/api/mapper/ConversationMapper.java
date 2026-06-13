package com.yoot.booking.api.mapper;

import com.yoot.booking.api.dto.message.ConversationResponseDTO;
import com.yoot.booking.api.entity.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel =
                MappingConstants
                        .ComponentModel
                        .SPRING
)
public interface ConversationMapper {

    // ================= CUSTOMER =================
    @Mapping(
            target = "customerId",
            source = "customer.id"
    )

    @Mapping(
            target = "customerName",
            expression =
                    "java(getDisplayName(entity.getCustomer()))"
    )

    @Mapping(
            target = "customerAvatar",
            source = "customer.avatar"
    )

    // ================= SUPPORT STAFF =================
    @Mapping(
            target = "supportStaffId",
            source = "supportStaff.id"
    )

    @Mapping(
            target = "supportStaffName",
            expression =
                    "java(getDisplayName(entity.getSupportStaff()))"
    )

    @Mapping(
            target = "supportStaffAvatar",
            source = "supportStaff.avatar"
    )

    // ================= LAST MESSAGE =================
    @Mapping(
            target = "lastMessage",
            ignore = true
    )

    @Mapping(
            target = "lastMessageTime",
            ignore = true
    )

    @Mapping(
            target = "unreadCount",
            ignore = true
    )

    ConversationResponseDTO toDTO(
            Conversation entity
    );

    // ================= DISPLAY NAME =================
    default String getDisplayName(
            com.yoot.booking.api.entity.User user
    ) {

        if (user == null) {
            return "Unknown User";
        }

        String fullName =
                user.getFullName();

        if (
                fullName != null &&
                        !fullName.isBlank()
        ) {
            return fullName;
        }

        return user.getEmail();
    }
}