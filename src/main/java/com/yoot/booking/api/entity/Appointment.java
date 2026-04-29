package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // User đặt lịch
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Nhân viên thực hiện
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    // Dịch vụ
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private BookingService service;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // trạng thái lịch
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    // trạng thái thanh toán
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
}