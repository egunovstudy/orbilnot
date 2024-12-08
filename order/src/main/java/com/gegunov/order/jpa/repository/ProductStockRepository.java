package com.gegunov.order.jpa.repository;

import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.Product;
import com.gegunov.order.jpa.model.ProductStock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ProductStockRepository extends CrudRepository<ProductStock, UUID> {
    List<ProductStock> findByOrder(Order order);

    @Query("select coalesce(sum(ps.quantity),0) from ProductStock ps where ps.product = ?1 and ps.status = ?2")
    int getSumQuantityByProductAndStatus(Product product, ProductStock.ProductStockStatus status);
    @Query("select coalesce(sum(ps.quantity),0) from ProductStock ps where ps.product = ?1 and ps.status in ( ?2 )")
    int getSumQuantityByProductAndStatusIn(Product product, Collection<ProductStock.ProductStockStatus> statuses);

}
