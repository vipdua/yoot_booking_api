package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= CUSTOMER =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    // ================= SUPPORT STAFF =================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_staff_id")
    private User supportStaff;

    // ================= STATUS =================
    @Column(name = "is_active")
    private Boolean isActive = true;

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