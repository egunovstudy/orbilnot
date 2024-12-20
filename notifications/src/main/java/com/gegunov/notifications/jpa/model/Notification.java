package com.gegunov.notifications.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "notification_service")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_service.notifications_id_seq")
    private Long id;

    private String title;
    private String body;
    @Column(name = "notification_type")
    private String notificationType;

    private UUID accountId;

}
