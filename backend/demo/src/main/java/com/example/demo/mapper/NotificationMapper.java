package com.example.demo.mapper;

import com.example.demo.Entity.Notification;
import com.example.demo.dto.NotificationResponsedto;

public class NotificationMapper {
    
    public static NotificationResponsedto toDto(Notification notification) {
        return new NotificationResponsedto(
            notification.getId(),
            notification.getMessage(),
            notification.getType(),
            notification.getRelatedEntityType(),
            notification.getRelatedEntityId(),
            notification.getIsRead(),
            notification.getCreatedAt()
        );
    }
}
