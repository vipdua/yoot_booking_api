package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC =================
    @Column(nullable = false)
    private String name;

    private String specialization;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ================= UI =================
    @Column(name = "avatar_url")
    private String avatarUrl;

    private String position;

    @Column(name = "experience_years")
    private Integer experienceYears;

    // ================= STATUS =================
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // ================= SERVICES =================
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "staff_services",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<BookingService> services = new ArrayList<>();
}