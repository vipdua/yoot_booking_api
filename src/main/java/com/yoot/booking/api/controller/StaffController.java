package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.staff.StaffCreateDTO;
import com.yoot.booking.api.dto.staff.StaffResponseDTO;
import com.yoot.booking.api.dto.staff.StaffUpdateDTO;
import com.yoot.booking.api.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResultListDTO<StaffResponseDTO> getAll(
            @ModelAttribute PagingRequestDTO request,
            @RequestParam(required = false) Long serviceId) {
        return staffService.getAll(request, serviceId);
    }

    @GetMapping("/available")
    public ResultListDTO<StaffResponseDTO> getAvailable(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @ModelAttribute PagingRequestDTO request) {
        return staffService.getAvailable(start, end, request);
    }

    @GetMapping("/{id}")
    public ResultDTO<StaffResponseDTO> getById(@PathVariable Long id) {
        return staffService.getById(id);
    }

    @PostMapping
    public ResultDTO<StaffResponseDTO> create(@RequestBody @Valid StaffCreateDTO dto) {
        return staffService.create(dto);
    }

    @PutMapping("/{id}")
    public ResultDTO<StaffResponseDTO> update(
            @PathVariable Long id,
            @RequestBody StaffUpdateDTO dto) {
        return staffService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return staffService.delete(id);
    }
}
