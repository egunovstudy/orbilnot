package com.gegunov.order.jpa.repository;

import com.gegunov.order.jpa.model.Ingredient;
import com.gegunov.order.jpa.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IngredientRepository extends CrudRepository<Ingredient, UUID> {
}
