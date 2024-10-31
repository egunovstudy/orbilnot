package com.gegunov.notifications.service;

import com.gegunov.model.PaymentEvent;
import com.gegunov.model.PaymentEventType;
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

    public List<NotificationDTO> getNotifications(UUID accountId) {
        List<Notification> byAccountId = notificationRepository.findByAccountId(accountId);
        NotificationsMapper mapper = Mappers.getMapper(NotificationsMapper.class);
        return mapper.toNotificationDTOList(byAccountId);

    }

    public void createNotification(PaymentEvent paymentEvent) {
        Notification notification = new Notification();
        notification.setAccountId(paymentEvent.getAccountId());
        notification.setNotificationType(paymentEvent.getPaymentEventType());

        notification.setTitle(buildNotificationTitle(paymentEvent));
        notification.setBody(paymentEvent.getDescription());
        notificationRepository.save(notification);
    }

    private String buildNotificationTitle(PaymentEvent paymentEvent) {
        if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_CONFIRMED) {
            return format("Order %s confirmed", paymentEvent.getOrderNumber());
        } else if (paymentEvent.getPaymentEventType() == PaymentEventType.PAYMENT_DENIED) {
            return format("Order %s denied", paymentEvent.getOrderNumber());
        }
        return format("Received unexpected notification for order %s", paymentEvent.getOrderNumber());
    }

}
