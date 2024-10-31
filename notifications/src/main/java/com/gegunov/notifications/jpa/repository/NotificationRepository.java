package com.gegunov.notifications.jpa.repository;

import com.gegunov.notifications.jpa.model.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findByAccountId(UUID accountId);
}
