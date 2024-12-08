package com.gegunov.foodstuff.jpa.repository;

import com.gegunov.foodstuff.jpa.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    Product findByCode(String code);
}
