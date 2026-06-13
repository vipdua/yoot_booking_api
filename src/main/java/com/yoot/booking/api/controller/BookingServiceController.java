package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.service.BookingServiceService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingServiceService service;

    // ================= GET ALL =================
    @GetMapping
    public ResultListDTO<BookingServiceResponseDTO> getAll(@ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    @GetMapping("/{id}") public ResultDTO<BookingServiceResponseDTO> getById( @PathVariable Long id ) {
        return service.getById(id);
    }

    // ================= CREATE =================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<BookingServiceResponseDTO> create(@ModelAttribute @Valid BookingServiceCreateDTO request) {
        return service.create(request);
    }

    // ================= UPDATE =================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<BookingServiceResponseDTO> update(@PathVariable Long id, @ModelAttribute BookingServiceUpdateDTO request) {
        return service.update(id, request);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}