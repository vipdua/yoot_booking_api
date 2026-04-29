package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;

public interface AppointmentService {

    ResultDTO<AppointmentResponseDTO> create(AppointmentCreateDTO dto);

    ResultDTO<AppointmentResponseDTO> confirm(Long id);

    ResultDTO<AppointmentResponseDTO> markPaid(Long id);

    ResultDTO<AppointmentResponseDTO> complete(Long id);

    ResultListDTO<AppointmentResponseDTO> getMyAppointments(PagingRequestDTO request);

    ResultListDTO<AppointmentResponseDTO> getAll(PagingRequestDTO request);

    ResultNoDataDTO cancel(Long id);

    ResultDTO<AppointmentResponseDTO> reschedule(Long id, AppointmentRescheduleDTO dto);
}