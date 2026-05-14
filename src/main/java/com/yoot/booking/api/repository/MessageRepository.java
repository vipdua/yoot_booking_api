package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // ================= GET BY CONVERSATION =================
    Page<Message> findAllByConversationIdOrderByCreatedAtAsc(Long conversationId, Pageable pageable);

    Optional<Message> findTopByConversationIdOrderByCreatedAtDesc(Long conversationId);
}