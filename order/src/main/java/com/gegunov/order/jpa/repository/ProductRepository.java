package com.gegunov.order.jpa.repository;

import com.gegunov.order.jpa.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    Product findByCode(String code);

}
