package com.gegunov.notifications.service;

import com.gegunov.model.KitchenStatus;
import com.gegunov.model.KitchenStatusEvent;
import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
import com.gegunov.notifications.EmailSender;
import com.gegunov.notifications.jpa.model.Notification;
import com.gegunov.notifications.jpa.repository.NotificationRepository;
import com.gegunov.notifications.mapping.NotificationsMapper;
import com.gegunov.notifications.web.model.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailSender emailSender;

    public List<NotificationDTO> getNotifications(UUID accountId) {
        List<Notification> byAccountId = notificationRepository.findByAccountId(accountId);
        NotificationsMapper mapper = Mappers.getMapper(NotificationsMapper.class);
        return mapper.toNotificationDTOList(byAccountId);

    }

    public void sendOrderReadyNotification(KitchenStatusEvent kitchenStatusEvent) throws Exception {
        Notification notification = new Notification();
        notification.setAccountId(kitchenStatusEvent.getAccountId());
        notification.setNotificationType(kitchenStatusEvent.getStatus().name());

        notification.setTitle(buildOrderReadyNotificationTitle(kitchenStatusEvent));
        notification.setBody("Thank you for your order.");
        emailSender.send("tube90@mail.ru", notification.getTitle(), notification.getBody());
    }

    public void sendPaymentNotification(PaymentEvent paymentEvent) throws Exception {
        Notification notification = new Notification();
        notification.setAccountId(paymentEvent.getAccountId());
        notification.setNotificationType(paymentEvent.getPaymentEventType().name());

        notification.setTitle(buildPaymentNotificationTitle(paymentEvent));
        notification.setBody(paymentEvent.getDescription());
        emailSender.send("tube90@mail.ru", notification.getTitle(), notification.getBody());
    }

    private String buildPaymentNotificationTitle(PaymentEvent paymentEvent) {
        if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_CONFIRMED) {
            return format("Order %s confirmed", paymentEvent.getOrderNumber());
        } else if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_DENIED) {
            return format("Order %s denied", paymentEvent.getOrderNumber());
        }
        return format("Received unexpected notification for order %s", paymentEvent.getOrderNumber());
    }

    private String buildOrderReadyNotificationTitle(KitchenStatusEvent kitchenStatusEvent) {
        if (kitchenStatusEvent.getStatus() == KitchenStatus.COOKING_COMPLETED) {
            return format("Order %s ready", kitchenStatusEvent.getOrderNumber());
        } else if (kitchenStatusEvent.getStatus() == KitchenStatus.CANCELLED) {
            return format("Order %s cancelled", kitchenStatusEvent.getOrderNumber());
        } else {
            return format("Received unexpected kitchen notification for order %s", kitchenStatusEvent.getOrderNumber());
        }
    }

}
