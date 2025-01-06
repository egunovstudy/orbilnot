package com.gegunov.foodstuff.service;

import com.gegunov.model.KitchenStatus;
import com.gegunov.model.KitchenStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KitchenStatusEventHandler {

    private final ReservationService reservationService;
    private final FoodstuffService foodstuffService;

    public void handleKitchenEvent(KitchenStatusEvent kitchenStatusEvent) {
        if (kitchenStatusEvent.getStatus() == KitchenStatus.COOKING_COMPLETED) {
            foodstuffService.changeReservedProductsToCooked(kitchenStatusEvent.getOrderNumber());
            reservationService.archiveReservations(kitchenStatusEvent.getOrderNumber());
        } else if (kitchenStatusEvent.getStatus() == KitchenStatus.CANCELLED) {
            foodstuffService.changeReservedProductsToAvailable(kitchenStatusEvent.getOrderNumber());
            reservationService.archiveReservations(kitchenStatusEvent.getOrderNumber());
        } else {
            throw new IllegalStateException("Unexpected kitchen status: " + kitchenStatusEvent.getStatus());
        }

    }

}
