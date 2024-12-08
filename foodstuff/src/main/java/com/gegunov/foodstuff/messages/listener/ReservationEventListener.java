package com.gegunov.foodstuff.messages.listener;

import com.gegunov.foodstuff.service.ReservationEventHandler;
import com.gegunov.model.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final ReservationEventHandler reservationEventHandler;

    @KafkaListener(topics = {"${reservation.events.topic}"}, groupId = "foodstuff-service-consumer")
    public void listen(ReservationEvent reservationEvent) {
        reservationEventHandler.handle(reservationEvent);
    }
}
