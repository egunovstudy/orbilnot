package com.gegunov.order.jpa.repository;

import com.gegunov.order.jpa.model.MenuItem;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MenuItemRepository extends CrudRepository<MenuItem, UUID> {

    MenuItem findByCode(String code);
}
