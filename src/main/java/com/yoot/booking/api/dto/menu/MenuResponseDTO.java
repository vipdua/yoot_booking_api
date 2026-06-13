package com.yoot.booking.api.dto.menu;

import com.yoot.booking.api.entity.MenuType;
import com.yoot.booking.api.entity.TargetType;

import java.util.ArrayList;
import java.util.List;

public class MenuResponseDTO {

    public Long id;
    public String name;
    public String slug;
    public String url;
    public Integer orderIndex;
    public Boolean isActive;
    public MenuType type;
    public TargetType target;
    public Long parentId;

    public List<MenuResponseDTO> children = new ArrayList<>();
}