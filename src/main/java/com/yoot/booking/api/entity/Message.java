package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= CONVERSATION =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    // ================= SENDER =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    // ================= CONTENT =================
    @Column(columnDefinition = "TEXT")
    private String content;

    // ================= FILE =================
    private String fileUrl;

    // ================= TYPE =================
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // ================= STATUS =================
    @Column(name = "is_seen")
    private Boolean isSeen = false;

    // ================= TIME =================
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = Instant.now();

        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = Instant.now();
    }
}