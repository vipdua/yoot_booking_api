package com.yoot.booking.api.repository;

import com.yoot.booking.api.entity.Menu;
import com.yoot.booking.api.entity.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByTypeAndIsActiveTrueOrderByOrderIndexAsc(MenuType type);

    List<Menu> findByParentIdAndType(Long parentId, MenuType type);

    boolean existsBySlugAndParentIdAndType(String slug, Long parentId, MenuType type);

    boolean existsBySlugAndParentIdAndTypeAndIdNot( String slug, Long parentId, MenuType type, Long id );

    boolean existsByParentId(Long parentId);
}