package com.gegunov.foodstuff.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_stock", schema = "foodstuff_service")
public class ProductStock {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    @Enumerated(EnumType.STRING)
    private ProductStockStatus status;

    public enum ProductStockStatus {
        NEW, AVAILABLE, RESERVED, WITHDRAWN, COOKED, ARCHIVE
    }

}
