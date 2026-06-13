package com.yoot.booking.api.service;

import com.yoot.booking.api.dto.Common.*;
import com.yoot.booking.api.dto.menu.*;
import com.yoot.booking.api.entity.MenuType;

public interface MenuService {

    ResultListDTO<MenuResponseDTO> getMenus(MenuType type);

    ResultDTO<MenuResponseDTO> create(MenuCreateDTO dto);

    ResultDTO<MenuResponseDTO> update(Long id, MenuUpdateDTO dto);

    ResultNoDataDTO delete(Long id);
}