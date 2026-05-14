package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;

import java.util.Map;

public interface AppointmentService {

    ResultDTO<AppointmentResponseDTO> create(AppointmentCreateDTO dto);

    ResultDTO<AppointmentResponseDTO> confirm(Long id);

    ResultDTO<AppointmentResponseDTO> markPaid(Long id);

    ResultDTO<AppointmentResponseDTO> complete(Long id);

    ResultDTO<VnPayPaymentResponseDTO> createPayment(Long id, String clientIp);

    String handleVnPayCallback(Map<String, String> params);

    ResultListDTO<AppointmentResponseDTO> getMyAppointments(PagingRequestDTO request);

    ResultListDTO<AppointmentResponseDTO> getAll(PagingRequestDTO request);

    ResultNoDataDTO cancel(Long id);

    ResultDTO<AppointmentResponseDTO> reschedule(Long id, AppointmentRescheduleDTO dto);

    ResultListDTO<AppointmentResponseDTO> getStaffAppointments(PagingRequestDTO request);

}