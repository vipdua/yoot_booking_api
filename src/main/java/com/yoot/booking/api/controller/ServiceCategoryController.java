package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.category.*;
import com.yoot.booking.api.service.ServiceCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class ServiceCategoryController {

    private final ServiceCategoryService service;

    // ================= GET ALL =================
    @GetMapping
    public ResultListDTO<ServiceCategoryResponseDTO> getAll(@ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResultDTO<ServiceCategoryResponseDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ================= CREATE =================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<ServiceCategoryResponseDTO> create(@RequestBody @Valid ServiceCategoryCreateDTO request) {
        return service.create(request);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<ServiceCategoryResponseDTO> update(@PathVariable Long id, @RequestBody ServiceCategoryUpdateDTO request) {
        return service.update(id, request);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}