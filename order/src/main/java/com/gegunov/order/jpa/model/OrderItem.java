package com.gegunov.order.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_items", schema = "order_service")
@Getter
@Setter
public class OrderItem {
    @Id
    private UUID id;
    private String name;
    private String code;
    private String description;
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_items_ingredients", schema = "order_service", joinColumns = {
            @JoinColumn(name = "order_item_id") }, inverseJoinColumns = { @JoinColumn(name = "ingredient_id") })
    private List<Ingredient> ingredients;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
