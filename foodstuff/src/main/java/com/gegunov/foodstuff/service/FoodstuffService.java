package com.gegunov.foodstuff.service;

import com.gegunov.foodstuff.jpa.model.Product;
import com.gegunov.foodstuff.jpa.model.ProductStock;
import com.gegunov.foodstuff.jpa.model.Reservation;
import com.gegunov.foodstuff.jpa.repository.ProductRepository;
import com.gegunov.foodstuff.jpa.repository.ProductStockRepository;
import com.gegunov.foodstuff.jpa.repository.ReservationRepository;
import com.gegunov.model.ProductDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodstuffService {

    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;
    private final ProductStockRepository productStockRepository;

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByCode(String code) {
        return productRepository.findByCode(code);
    }

    @Transactional
    public void changeProductQuantity(ProductDTO stockEvent) {
        productStockRepository.findById(stockEvent.getRequestUuid()).ifPresent(r -> {
            throw new RuntimeException("This event has already processed");
        });
        Product product = getProductByCode(stockEvent.getCode());
        ProductStock productStock = new ProductStock();
        productStock.setQuantity(stockEvent.getQuantity());
        productStock.setProduct(product);
        productStock.setId(stockEvent.getRequestUuid());
        productStock.setStatus(ProductStock.ProductStockStatus.NEW);
        productStockRepository.save(productStock);
    }

    @Transactional
    public void createOrUpdateProduct(Product product) {
        Product fromDb = productRepository.findByCode(product.getCode());
        if (fromDb == null) {
            product.setId(UUID.randomUUID());
            productRepository.save(product);
        } else {
            fromDb.setName(product.getName());
            fromDb.setDescription(product.getDescription());
            productRepository.save(fromDb);
        }

    }

    @Transactional
    public void changeReservedProductsToCooked(String orderNumber) {
        List<Reservation> reservations = reservationRepository.findByOrderNumberAndStatus(orderNumber,
                Reservation.Status.ACTIVE);
        updateProductStock(reservations, ProductStock.ProductStockStatus.COOKED);
    }

    @Transactional
    public void changeReservedProductsToInArchive(String orderNumber) {
        List<Reservation> reservations = reservationRepository.findByOrderNumberAndStatus(orderNumber,
                Reservation.Status.ACTIVE);
        updateProductStock(reservations, ProductStock.ProductStockStatus.ARCHIVE);
    }

    private void updateProductStock(List<Reservation> reservations, ProductStock.ProductStockStatus inSync) {
        reservations.forEach((Reservation reservation) -> {
            for (ProductStock productStock : reservation.getProductStock()) {
                productStock.setStatus(inSync);
                productStockRepository.save(productStock);
            }
        });
    }
}
