package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;
import com.yoot.booking.api.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResultDTO<AppointmentResponseDTO> create(
            @RequestBody @Valid AppointmentCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/me")
    public ResultListDTO<AppointmentResponseDTO> getMy(
            @ModelAttribute PagingRequestDTO request) {
        return service.getMyAppointments(request);
    }

    @GetMapping
    public ResultListDTO<AppointmentResponseDTO> getAll(
            @ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    @PutMapping("/{id}/cancel")
    public ResultNoDataDTO cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    @PutMapping("/{id}/reschedule")
    public ResultDTO<AppointmentResponseDTO> reschedule(
            @PathVariable Long id,
            @RequestBody @Valid AppointmentRescheduleDTO dto) {
        return service.reschedule(id, dto);
    }
}