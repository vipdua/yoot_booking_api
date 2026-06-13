package com.yoot.booking.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    private String url;

    private Integer orderIndex;

    @Column(name = "is_active")
    private Boolean isActive;

    // HEADER / FOOTER
    @Enumerated(EnumType.STRING)
    private MenuType type;

    // _self / _blank
    @Enumerated(EnumType.STRING)
    private TargetType target;

    // menu cha
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Menu parent;
}