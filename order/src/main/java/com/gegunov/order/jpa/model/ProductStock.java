package com.gegunov.order.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_stock", schema = "order_service")
public class ProductStock {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Enumerated(EnumType.STRING)
    private ProductStockStatus status;

    public enum ProductStockStatus {
        AVAILABLE, RESERVED, WITHDRAWN, COOKED, ARCHIVE
    }

}
