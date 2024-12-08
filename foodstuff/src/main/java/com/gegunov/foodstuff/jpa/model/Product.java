package com.gegunov.foodstuff.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products", schema = "foodstuff_service")
public class Product {
    @Id
    private UUID id;
    private String name;
    private String description;
    private String code;
}
