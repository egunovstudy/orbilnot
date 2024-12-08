package com.gegunov.foodstuff.service;

import com.gegunov.foodstuff.messages.sender.ReservationStatusEventSender;
import com.gegunov.model.IngredientDTO;
import com.gegunov.model.ReservationEvent;
import com.gegunov.model.ReservationStatusEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventHandler {

    private final ReservationService reservationService;
    private final ReservationStatusEventSender reservationStatusEventSender;

    @Transactional
    public void handle(ReservationEvent reservationEvent) {
        try {
            reservationService.makeReservationIfEnough(reservationEvent);
            reservationStatusEventSender.sendReservationStatus(
                    new ReservationStatusEvent(reservationEvent.getAccountId(), reservationEvent.getOrderNumber(),
                            ReservationStatusEvent.State.SUCCESS));
        } catch (Exception e) {
            reservationStatusEventSender.sendReservationStatus(
                    new ReservationStatusEvent(reservationEvent.getAccountId(), reservationEvent.getOrderNumber(),
                            ReservationStatusEvent.State.FAILED));
        }

    }

}
