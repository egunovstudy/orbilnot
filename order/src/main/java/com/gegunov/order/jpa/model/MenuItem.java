package com.gegunov.order.jpa.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "menu_items", schema = "order_service")
@Getter
@Setter
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class MenuItem {
    @Id
    private UUID id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    @Transient
    private Integer availableQuantity;

    @OneToMany(mappedBy = "menuItem")
    private List<Ingredient> ingredients;
}
