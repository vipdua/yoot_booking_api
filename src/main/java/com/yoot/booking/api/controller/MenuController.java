package com.yoot.booking.api.controller;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.menu.MenuCreateDTO;
import com.yoot.booking.api.dto.menu.MenuResponseDTO;
import com.yoot.booking.api.dto.menu.MenuUpdateDTO;
import com.yoot.booking.api.entity.MenuType;
import com.yoot.booking.api.service.MenuService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService service;

    // ================= GET MENU =================
    @GetMapping
    public ResultListDTO<MenuResponseDTO> getMenus(@RequestParam MenuType type) {
        return service.getMenus(type);
    }

    // ================= CREATE =================
    @PostMapping
    public ResultDTO<MenuResponseDTO> create(@RequestBody MenuCreateDTO dto) {
        return service.create(dto);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResultDTO<MenuResponseDTO> update(@PathVariable Long id, @RequestBody MenuUpdateDTO dto) {
        return service.update(id, dto);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResultNoDataDTO delete(@PathVariable Long id) {
        return service.delete(id);
    }
}