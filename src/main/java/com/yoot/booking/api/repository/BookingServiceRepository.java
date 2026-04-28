package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
}