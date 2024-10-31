package com.gegunov.notifications.web;

import com.gegunov.notifications.service.NotificationService;
import com.gegunov.notifications.web.model.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class NotificationsController {
    private final NotificationService notificationService;

    @GetMapping(path = "/{accountId}")
    public List<NotificationDTO> getNotifications(@PathVariable("accountId") UUID accountId) {
        return notificationService.getNotifications(accountId);
    }

}
