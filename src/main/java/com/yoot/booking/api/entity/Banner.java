package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    private String videoUrl;

    private String ctaText;

    private String ctaLink;

    @Enumerated(EnumType.STRING)
    private BannerPosition position;

    @Column(name = "is_active")
    private Boolean isActive;

    private Integer orderIndex;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}