package com.example.demo.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Notification;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Service.NotificationService;
import com.example.demo.dto.NotificationResponsedto;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final UserRepo userRepo;
    
    @GetMapping("/{email}")
    public ResponseEntity<Apiresponse<List<NotificationResponsedto>>> getNotifications(@PathVariable String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        
        List<Notification> notifications = notificationService.getNotifications(user);
        List<NotificationResponsedto> dtoList = notifications.stream()
            .map(NotificationMapper::toDto)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(new Apiresponse<>("Notifications fetched successfully", 200, dtoList));
    }

    @GetMapping
    public ResponseEntity<Apiresponse<List<NotificationResponsedto>>> getCurrentNotifications(Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));

        List<Notification> notifications = notificationService.getNotifications(user);
        List<NotificationResponsedto> dtoList = notifications.stream()
            .map(NotificationMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(new Apiresponse<>("Notifications fetched successfully", 200, dtoList));
    }
    
    @GetMapping("/{email}/unread")
    public ResponseEntity<Apiresponse<List<NotificationResponsedto>>> getUnreadNotifications(@PathVariable String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        
        List<Notification> notifications = notificationService.getUnreadNotifications(user);
        List<NotificationResponsedto> dtoList = notifications.stream()
            .map(NotificationMapper::toDto)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(new Apiresponse<>("Unread notifications fetched successfully", 200, dtoList));
    }

    @GetMapping("/unread")
    public ResponseEntity<Apiresponse<List<NotificationResponsedto>>> getCurrentUnreadNotifications(Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));

        List<Notification> notifications = notificationService.getUnreadNotifications(user);
        List<NotificationResponsedto> dtoList = notifications.stream()
            .map(NotificationMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(new Apiresponse<>("Unread notifications fetched successfully", 200, dtoList));
    }
    
    @GetMapping("/{email}/unread/count")
    public ResponseEntity<Apiresponse<Long>> getUnreadCount(@PathVariable String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        
        long count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(new Apiresponse<>("Unread count", 200, count));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Apiresponse<Long>> getCurrentUnreadCount(Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));

        long count = notificationService.getUnreadCount(user);
        return ResponseEntity.ok(new Apiresponse<>("Unread count", 200, count));
    }
    
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Apiresponse<NotificationResponsedto>> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        if (notification == null) {
            throw new com.example.demo.exception.ResourceNotFoundException("Notification not found");
        }
        return ResponseEntity.ok(new Apiresponse<>("Notification marked as read", 200, NotificationMapper.toDto(notification)));
    }
    
    @PutMapping("/{email}/read-all")
    public ResponseEntity<Apiresponse<String>> markAllAsRead(@PathVariable String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));
        
        notificationService.markAllAsRead(user);
        return ResponseEntity.ok(new Apiresponse<>("All notifications marked as read", 200, "Success"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Apiresponse<String>> markCurrentAllAsRead(Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
            .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("User not found"));

        notificationService.markAllAsRead(user);
        return ResponseEntity.ok(new Apiresponse<>("All notifications marked as read", 200, "Success"));
    }
    
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Apiresponse<String>> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(new Apiresponse<>("Notification deleted successfully", 200, "Success"));
    }
}
