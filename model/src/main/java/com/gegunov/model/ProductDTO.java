package com.gegunov.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDTO {
    private UUID requestUuid;
    private String code;
    private Integer quantity;
}
