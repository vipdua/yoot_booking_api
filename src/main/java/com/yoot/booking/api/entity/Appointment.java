package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= USER =================
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ================= STAFF =================
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    // ================= SERVICE =================
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private BookingService service;

    // ================= CUSTOMER INFO =================
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(columnDefinition = "TEXT")
    private String note;

    // ================= TIME =================
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // ================= PRICE =================
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // ================= STATUS =================
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    // ================= PAYMENT =================
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
}