package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.blockedslot.*;
import com.yoot.booking.api.service.BlockedSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blocked-slots")
@RequiredArgsConstructor
public class BlockedSlotController {

    private final BlockedSlotService service;

    @PostMapping
    public ResultDTO<BlockedSlotResponseDTO> create(@RequestBody @Valid BlockedSlotCreateDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public ResultListDTO<BlockedSlotResponseDTO> getAll(@ModelAttribute PagingRequestDTO request) {
        return service.getAll(request);
    }

    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}