package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer duration; // phút

    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}