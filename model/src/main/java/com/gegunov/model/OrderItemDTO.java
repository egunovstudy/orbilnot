package com.gegunov.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItemDTO {
    private List<IngredientDTO> ingredients;
    private String name;
}
