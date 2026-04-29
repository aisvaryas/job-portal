package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Notification;
import com.example.demo.Entity.User;
import com.example.demo.Repository.NotificationRepo;
import com.example.demo.dto.NotificationResponsedto;
import com.example.demo.mapper.NotificationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepo notificationRepo;
    
    public Notification createNotification(User recipient, String message, String type, 
                                          String relatedEntityType, Long relatedEntityId) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedEntityType(relatedEntityType);
        notification.setRelatedEntityId(relatedEntityId);
        notification.setIsRead(false);
        return notificationRepo.save(notification);
    }
    
    public List<Notification> getNotifications(User user) {
        return notificationRepo.findByRecipientOrderByCreatedAtDesc(user);
    }
    
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepo.findByRecipientAndIsReadFalse(user);
    }
    
    public long getUnreadCount(User user) {
        return notificationRepo.countByRecipientAndIsReadFalse(user);
    }
    
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepo.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            return notificationRepo.save(notification);
        }
        return null;
    }
    
    public void markAllAsRead(User user) {
        List<Notification> notifications = notificationRepo.findByRecipientAndIsReadFalse(user);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepo.saveAll(notifications);
    }
    
    public void deleteNotification(Long notificationId) {
        notificationRepo.deleteById(notificationId);
    }
}
