package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    // ================= FIND EXIST CONVERSATION =================
    Optional<Conversation>
    findByCustomerIdAndSupportStaffId(

            Long customerId,

            Long supportStaffId
    );

    // ================= CUSTOMER CONVERSATIONS =================
    List<Conversation>
    findAllByCustomerIdOrderByUpdatedAtDesc(

            Long customerId
    );

    // ================= SUPPORT STAFF CONVERSATIONS =================
    List<Conversation>
    findAllBySupportStaffIdOrderByUpdatedAtDesc(

            Long supportStaffId
    );
}