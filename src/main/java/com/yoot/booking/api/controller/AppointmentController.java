package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.appointment.*;
import com.yoot.booking.api.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<AppointmentResponseDTO> create(@RequestBody @Valid AppointmentCreateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResultDTO<AppointmentResponseDTO> confirm(@PathVariable Long id) {
        return service.confirm(id);
    }

    @PutMapping("/{id}/paid")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResultDTO<AppointmentResponseDTO> markPaid(@PathVariable Long id) {
        return service.markPaid(id);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResultDTO<AppointmentResponseDTO> complete(@PathVariable Long id) {
        return service.complete(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResultListDTO<AppointmentResponseDTO> getMy(@ModelAttribute PagingRequestDTO request) {
        return service.getMyAppointments(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResultListDTO<AppointmentResponseDTO> getAll(@ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResultNoDataDTO cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("isAuthenticated()")
    public ResultDTO<AppointmentResponseDTO> reschedule(
            @PathVariable Long id,
            @RequestBody @Valid AppointmentRescheduleDTO dto) {
        return service.reschedule(id, dto);
    }
}