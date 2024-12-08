package com.gegunov.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDTO {
    private ProductDTO product;
    private int requiredAmount;
}
