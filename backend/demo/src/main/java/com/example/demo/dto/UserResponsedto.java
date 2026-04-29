package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsedto {

    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Boolean isActive;

   
}