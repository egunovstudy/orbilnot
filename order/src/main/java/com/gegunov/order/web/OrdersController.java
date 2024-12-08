package com.gegunov.order.web;

import com.gegunov.order.service.OrderService;
import com.gegunov.order.web.model.OrderAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping(path = "/{accountId}")
    public List<OrderAction> getOrders(@PathVariable("accountId") UUID accountId) {
        return orderService.getOrders(accountId);
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody OrderAction orderAction) {
        return ResponseEntity.ok(orderService.createOrderAndReserveProductStock(orderAction));
    }


    @DeleteMapping
    public void delete(@RequestBody OrderAction orderAction) {
        orderService.deleteOrder(orderAction.getAccountId(), orderAction.getOrderNumber());
    }

}
