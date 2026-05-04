package com.yoot.booking.api.service;

import com.yoot.booking.api.entity.Appointment;

public interface EmailService {
    void sendAppointmentPaidEmail(Appointment appointment);

    void sendAppointmentConfirmedEmail(Appointment appointment);
}