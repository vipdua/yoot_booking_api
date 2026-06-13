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

    // ================= GET ALL =================
    ResultListDTO<StaffResponseDTO> getAll(
            PagingRequestDTO request,
            Long serviceId
    );

    // ================= GET BY ID =================
    ResultDTO<StaffResponseDTO> getById(Long id);

    // ================= GET AVAILABLE =================
    ResultListDTO<StaffResponseDTO> getAvailable(
            Long serviceId,
            LocalDateTime start,
            LocalDateTime end,
            PagingRequestDTO request
    );

    // ================= CREATE =================
    ResultDTO<StaffResponseDTO> create(
            StaffCreateDTO dto
    );

    // ================= UPDATE =================
    ResultDTO<StaffResponseDTO> update(
            Long id,
            StaffUpdateDTO dto
    );

    // ================= DELETE =================
    ResultNoDataDTO delete(Long id);
}