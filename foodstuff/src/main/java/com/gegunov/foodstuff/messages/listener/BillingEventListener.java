package com.gegunov.foodstuff.messages.listener;

import com.gegunov.foodstuff.service.FoodstuffService;
import com.gegunov.foodstuff.service.ReservationService;
import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingEventListener {

    private final FoodstuffService foodstuffService;
    private final ReservationService reservationService;

    @KafkaListener(topics = { "${payment.status.events.topic}" }, groupId = "foodstuff-service-consumer")
    public void listen(ConsumerRecord<String, PaymentEvent> record) {
        PaymentEvent paymentEvent = record.value();

        if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_DENIED) {
            foodstuffService.changeReservedProductsToInArchive(paymentEvent.getOrderNumber());
            reservationService.archiveReservations(paymentEvent.getOrderNumber());
        }
    }
}
