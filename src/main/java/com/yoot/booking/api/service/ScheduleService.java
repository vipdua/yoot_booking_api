package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.schedule.*;

import java.time.LocalDate;

public interface ScheduleService {

    ResultListDTO<ScheduleResponseDTO> getAll(PagingRequestDTO request, Long staffId, LocalDate date);

    ResultDTO<ScheduleResponseDTO> create(ScheduleCreateDTO dto);

    ResultDTO<ScheduleResponseDTO> update(Long id, ScheduleUpdateDTO dto);

    ResultNoDataDTO delete(Long id);
}
