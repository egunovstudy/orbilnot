package com.gegunov.notifications.mapping;

import com.gegunov.notifications.jpa.model.Notification;
import com.gegunov.notifications.web.model.NotificationDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface NotificationsMapper {

    NotificationDTO toNotificationDTO(Notification notification);

    List<NotificationDTO> toNotificationDTOList(List<Notification> notifications);
}
