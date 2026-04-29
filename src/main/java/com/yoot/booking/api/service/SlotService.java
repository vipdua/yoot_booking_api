package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.slot.SlotResponseDTO;

import java.time.LocalDate;

public interface SlotService {

    ResultListDTO<SlotResponseDTO> getAvailableSlots(LocalDate date, Long staffId, Long serviceId, PagingRequestDTO request);
}