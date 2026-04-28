package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.bookingservice.*;
import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.service.BookingServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingServiceService service;

    @GetMapping
    public ResultListDTO<BookingServiceResponseDTO> getAll(@ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    @PostMapping
    public ResultDTO<BookingServiceResponseDTO> create(@RequestBody @Valid BookingServiceCreateDTO request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ResultDTO<BookingServiceResponseDTO> update(@PathVariable Long id, @RequestBody BookingServiceUpdateDTO request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}