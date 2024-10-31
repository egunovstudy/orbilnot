package com.gegunov.notifications.messages.listener;

import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
import com.gegunov.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = { "${payment.events.topic}" }, groupId = "notification-service-consumer")
    public void listen(PaymentEvent paymentEvent) {
        notificationService.createNotification(paymentEvent);
    }
}
