package com.example.demo.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Column(nullable = false, length = 50)
    private String type; // APPLICATION, SHORTLIST, REJECT, HIRE, JOB_CLOSED, NEW_APPLICATION
    
    @Column(length = 50)
    private String relatedEntityType; // JOB, APPLICATION
    
    private Long relatedEntityId;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
