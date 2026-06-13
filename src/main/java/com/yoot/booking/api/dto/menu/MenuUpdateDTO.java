package com.yoot.booking.api.dto.menu;

import com.yoot.booking.api.entity.MenuType;
import com.yoot.booking.api.entity.TargetType;

public record MenuUpdateDTO(
        String name,
        String slug,
        String url,
        Integer orderIndex,
        Boolean isActive,
        MenuType type,
        TargetType target,
        Long parentId
) {}