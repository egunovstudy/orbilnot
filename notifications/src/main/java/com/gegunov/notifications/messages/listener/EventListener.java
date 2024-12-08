package com.gegunov.notifications.messages.listener;

import com.gegunov.model.KitchenStatusEvent;
import com.gegunov.model.PaymentEvent;
import com.gegunov.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class EventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = { "${payment.status.events.topic}" }, groupId = "notification-service-consumer")
    public void listenPaymentEvent(PaymentEvent paymentEvent) throws Exception {
        notificationService.sendPaymentNotification(paymentEvent);
    }

    @KafkaListener(topics = { "${kitchen.status.events.topic}" }, groupId = "notification-service-consumer")
    public void listen(KitchenStatusEvent kitchenStatusEvent) throws Exception {
        notificationService.sendOrderReadyNotification(kitchenStatusEvent);
    }
}
