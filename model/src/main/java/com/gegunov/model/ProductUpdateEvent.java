package com.gegunov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateEvent {
    private String code;
    private Integer quantity;
    private UUID stockId;
    private String name;
    private String description;

}
