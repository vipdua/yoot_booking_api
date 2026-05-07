package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.dto.Common.ResultDTO;
import com.yoot.booking.api.dto.Common.ResultListDTO;
import com.yoot.booking.api.dto.Common.ResultNoDataDTO;
import com.yoot.booking.api.dto.menu.MenuCreateDTO;
import com.yoot.booking.api.dto.menu.MenuResponseDTO;
import com.yoot.booking.api.dto.menu.MenuUpdateDTO;
import com.yoot.booking.api.entity.Menu;
import com.yoot.booking.api.entity.MenuType;
import com.yoot.booking.api.mapper.MenuMapper;
import com.yoot.booking.api.repository.MenuRepository;
import com.yoot.booking.api.service.MenuService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;
    private final MenuMapper mapper;

    // ================= GET MENU =================
    @Override
    public ResultListDTO<MenuResponseDTO> getMenus(MenuType type) {

        var menus = repository.findByTypeAndIsActiveTrueOrderByOrderIndexAsc(type);

        if (menus.isEmpty()) {
            return ResultListDTO.success(List.of(), "Danh sách menu rỗng", null);
        }

        var dtoList = menus.stream()
                .map(mapper::toDTO)
                .toList();

        var tree = buildTree(dtoList);

        return ResultListDTO.success(tree, "Lấy menu thành công", null);
    }

    // ================= CREATE =================
    @Override
    public ResultDTO<MenuResponseDTO> create(MenuCreateDTO dto) {

        Menu entity = mapper.toEntity(dto);

        // set parent
        Menu parent = null;
        if (dto.parentId() != null) {
            parent = repository.findById(dto.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu không tồn tại"));
            entity.setParent(parent);
        }

        // check duplicate slug (cùng parent + type)
        boolean exists = repository.existsBySlugAndParentIdAndType(
                dto.slug(),
                dto.parentId(),
                dto.type()
        );

        if (exists) {
            throw new RuntimeException("Slug đã tồn tại trong menu");
        }

        // auto orderIndex
        int maxIndex = repository
                .findByParentIdAndType(dto.parentId(), dto.type())
                .stream()
                .mapToInt(Menu::getOrderIndex)
                .max()
                .orElse(0);

        entity.setOrderIndex(maxIndex + 1);

        var saved = repository.save(entity);

        return ResultDTO.success(mapper.toDTO(saved), "Tạo menu thành công");
    }

    // ================= UPDATE =================
    @Override
    public ResultDTO<MenuResponseDTO> update(Long id, MenuUpdateDTO dto) {

        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu không tồn tại"));

        mapper.updateEntity(dto, entity);

        // update parent nếu có
        if (dto.parentId() != null) {
            Menu parent = repository.findById(dto.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent không tồn tại"));

            entity.setParent(parent);
        }

        return ResultDTO.success(mapper.toDTO(repository.save(entity)), "Cập nhật menu thành công");
    }

    // ================= DELETE =================
    @Override
    public ResultNoDataDTO delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Menu không tồn tại");
        }

        repository.deleteById(id);

        return ResultNoDataDTO.success("Xóa menu thành công");
    }

    // ================= BUILD TREE =================
    private List<MenuResponseDTO> buildTree(List<MenuResponseDTO> list) {

        Map<Long, MenuResponseDTO> map = new HashMap<>();

        for (var item : list) {
            map.put(item.id, item);
        }

        List<MenuResponseDTO> roots = new ArrayList<>();

        for (var item : list) {

            if (item.parentId == null) {
                roots.add(item);
            } else {
                var parent = map.get(item.parentId);

                if (parent != null) {
                    parent.children.add(item);
                }
            }
        }

        return roots;
    }
}