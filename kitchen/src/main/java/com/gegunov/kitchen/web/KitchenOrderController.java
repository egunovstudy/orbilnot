package com.gegunov.kitchen.web;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchen")
@RequiredArgsConstructor
public class KitchenOrderController {

    private final KitchenOrderService kitchenOrderService;

    @PostMapping
    public void create(KitchenOrder kitchenOrder) {
        kitchenOrderService.createKitchenOrder(kitchenOrder);
    }

    @PutMapping
    public void update(KitchenOrder kitchenOrder) {
        kitchenOrderService.updateKitchenOrder(kitchenOrder);
    }

}
