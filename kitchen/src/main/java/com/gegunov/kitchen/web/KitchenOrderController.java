package com.gegunov.kitchen.web;

import com.gegunov.kitchen.jpa.model.KitchenOrder;
import com.gegunov.kitchen.service.KitchenOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class KitchenOrderController {

    private final KitchenOrderService kitchenOrderService;

    @PostMapping
    public void create(@RequestBody KitchenOrder kitchenOrder) {
        kitchenOrderService.createKitchenOrder(kitchenOrder);
    }

    @PutMapping
    public void update(@RequestBody KitchenOrder kitchenOrder) {
        kitchenOrderService.updateKitchenOrder(kitchenOrder);
    }

}
