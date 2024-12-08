package com.gegunov.foodstuff.service;

import com.gegunov.foodstuff.jpa.model.Product;
import com.gegunov.foodstuff.jpa.model.ProductStock;
import com.gegunov.foodstuff.jpa.model.Reservation;
import com.gegunov.foodstuff.jpa.repository.ProductRepository;
import com.gegunov.foodstuff.jpa.repository.ProductStockRepository;
import com.gegunov.foodstuff.jpa.repository.ReservationRepository;
import com.gegunov.model.IngredientDTO;
import com.gegunov.model.ReservationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final List<ProductStock.ProductStockStatus> NEGATIVE_STATUSES = List.of(
            ProductStock.ProductStockStatus.COOKED, ProductStock.ProductStockStatus.RESERVED,
            ProductStock.ProductStockStatus.WITHDRAWN);
    private final ReservationRepository reservationRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;

    public void makeReservationIfEnough(ReservationEvent reservationEvent) {

        Reservation reservationEntity = new Reservation();
        reservationEntity.setOrderNumber(reservationEvent.getOrderNumber());
        reservationEntity.setAccountId(reservationEvent.getAccountId());
        reservationEntity.setStatus(Reservation.Status.ACTIVE);
        reservationEntity.setId(UUID.randomUUID());
        reservationRepository.save(reservationEntity);

        for (IngredientDTO ingredientDTO : reservationEvent.getIngredients()) {
            String productCode = ingredientDTO.getProduct().getCode();
            Product product = productRepository.findByCode(productCode);
            validateProductQuantity(ingredientDTO.getRequiredAmount(), product);
            ProductStock productStock = new ProductStock();
            productStock.setProduct(product);
            productStock.setQuantity(ingredientDTO.getRequiredAmount());
            productStock.setId(UUID.randomUUID());
            productStock.setStatus(ProductStock.ProductStockStatus.RESERVED);
            productStockRepository.save(productStock);
        }

    }

    public void archiveReservations(String orderNumber) {
        reservationRepository.updateStatusByOrderNumberAndStatus(Reservation.Status.ARCHIVE, orderNumber,
                Reservation.Status.ACTIVE);
    }

    private void validateProductQuantity(Integer quantityRequested, Product product) {

        Integer sumOfActive = productStockRepository.getSumQuantityByProductAndStatus(product,
                ProductStock.ProductStockStatus.AVAILABLE);
        Integer sumOfInactive = productStockRepository.getSumQuantityByProductAndStatusIn(product, NEGATIVE_STATUSES);

        int remains = sumOfActive - sumOfInactive;
        if (remains - quantityRequested < 0) {
            throw new ReservationException("The foodstuff does not have enough stock of "
                    + product.getCode()
                    + " to reserve. Requested "
                    + quantityRequested
                    + " but only"
                    + (remains)
                    + " remained");
        }
    }

}
