package com.gegunov.order.service;

import com.gegunov.model.ProductUpdateEvent;
import com.gegunov.order.jpa.model.Order;
import com.gegunov.order.jpa.model.Product;
import com.gegunov.order.jpa.model.ProductStock;
import com.gegunov.order.jpa.repository.ProductStockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gegunov.order.jpa.model.ProductStock.ProductStockStatus.*;

@Service
@RequiredArgsConstructor
public class ProductStockService {

    private final ProductStockRepository productStockRepository;

    @Transactional
    public void archiveProductStock(Order order) {
        List<ProductStock> productStocks = productStockRepository.findByOrder(order);
        for (ProductStock productStock : productStocks) {
            productStock.setStatus(ARCHIVE);
        }
    }

    @Transactional
    public void makeProductStockCooked(Order order) {
        List<ProductStock> productStocks = productStockRepository.findByOrder(order);
        for (ProductStock productStock : productStocks) {
            productStock.setStatus(COOKED);
        }
    }

    @Transactional
    public void createAvailableProductStock(Product product, ProductUpdateEvent productUpdateEvent) {
        createProductStock(product, null, AVAILABLE, productUpdateEvent.getQuantity(), productUpdateEvent.getStockId());
    }

    @Transactional
    public void createReservedProductStock(Product product, Order order, int quantity) {
        UUID id = UUID.randomUUID();
        createProductStock(product, order, RESERVED, quantity, id);
    }

    private void createProductStock(Product product, Order order, ProductStock.ProductStockStatus status,
            int quantity, UUID id) {
        ProductStock productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setStatus(status);
        productStock.setOrder(order);
        productStock.setId(id);
        productStock.setQuantity(quantity);
        productStockRepository.save(productStock);
    }

}
