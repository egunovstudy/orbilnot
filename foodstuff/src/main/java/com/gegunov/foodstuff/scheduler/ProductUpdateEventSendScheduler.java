package com.gegunov.foodstuff.scheduler;

import com.gegunov.foodstuff.jpa.model.ProductStock;
import com.gegunov.foodstuff.jpa.repository.ProductStockRepository;
import com.gegunov.foodstuff.messages.sender.ProductUpdateEventSender;
import com.gegunov.model.ProductUpdateEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductUpdateEventSendScheduler {

    private final ProductStockRepository productStockRepository;
    private final ProductUpdateEventSender productUpdateEventSender;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void sendProductUpdateEvent(){
        List<ProductStock> newProductStocks = productStockRepository.findByStatus(ProductStock.ProductStockStatus.NEW);
        for (ProductStock productStock : newProductStocks) {
            productStock.setStatus(ProductStock.ProductStockStatus.AVAILABLE);
            productStockRepository.save(productStock);
            productUpdateEventSender.sendProductUpdateEvent(
                    new ProductUpdateEvent(productStock.getProduct().getCode(), productStock.getQuantity(), productStock.getId(), productStock.getProduct()
                            .getName(), productStock.getProduct().getDescription()));
        }
    }



}
