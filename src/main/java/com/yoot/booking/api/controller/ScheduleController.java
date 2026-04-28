package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.PagingRequestDTO;
import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.schedule.ScheduleCreateDTO;
import com.yoot.booking.api.dto.schedule.ScheduleResponseDTO;
import com.yoot.booking.api.dto.schedule.ScheduleUpdateDTO;
import com.yoot.booking.api.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public ResultDTO<ScheduleResponseDTO> getById(@PathVariable Long id) {
        return scheduleService.getById(id);
    }

    @GetMapping
    public ResultListDTO<ScheduleResponseDTO> getAll(
            @ModelAttribute PagingRequestDTO request,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return scheduleService.getAll(request, staffId, date);
    }

    @PostMapping
    public ResultDTO<ScheduleResponseDTO> create(@RequestBody @Valid ScheduleCreateDTO dto) {
        return scheduleService.create(dto);
    }

    @PutMapping("/{id}")
    public ResultDTO<ScheduleResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateDTO dto) {
        return scheduleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return scheduleService.delete(id);
    }
}
