package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.banner.*;
import com.yoot.booking.api.entity.BannerPosition;
import com.yoot.booking.api.service.BannerService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService service;

    // ================= GET ACTIVE =================
    @GetMapping
    public ResultListDTO<BannerResponseDTO> getByPosition(@RequestParam BannerPosition position) {
        return service.getByPosition(position);
    }

    // ================= CREATE =================
    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultDTO<BannerResponseDTO> create(@ModelAttribute @Valid BannerCreateDTO dto) {
        return service.create(dto);
    }

    // ================= UPDATE =================
    @PutMapping(value = "/{id}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultDTO<BannerResponseDTO> update(@PathVariable Long id, @ModelAttribute BannerUpdateDTO dto) {
        return service.update(id, dto);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}