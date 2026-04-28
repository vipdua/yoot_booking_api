package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.staff.StaffCreateDTO;
import com.yoot.booking.api.dto.staff.StaffResponseDTO;
import com.yoot.booking.api.dto.staff.StaffUpdateDTO;

import java.time.LocalDateTime;

public interface StaffService {

    ResultListDTO<StaffResponseDTO> getAll(PagingRequestDTO request, Long serviceId);

    ResultDTO<StaffResponseDTO> getById(Long id);

    ResultListDTO<StaffResponseDTO> getAvailable(LocalDateTime start, LocalDateTime end, PagingRequestDTO request);

    ResultDTO<StaffResponseDTO> create(StaffCreateDTO dto);

    ResultDTO<StaffResponseDTO> update(Long id, StaffUpdateDTO dto);

    ResultNoDataDTO delete(Long id);
}
