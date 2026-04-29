package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Notification;
import com.example.demo.Entity.User;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    
    List<Notification> findByRecipientAndIsReadFalse(User recipient);
    
    long countByRecipientAndIsReadFalse(User recipient);
}
