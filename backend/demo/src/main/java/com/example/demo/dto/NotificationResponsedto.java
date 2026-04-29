package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponsedto {
    private Long id;
    private String message;
    private String type;
    private String relatedEntityType;
    private Long relatedEntityId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
