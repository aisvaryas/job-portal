package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponsedto {
    private String token;
    private Long userId;
    private String email;
    private String role;
    private Long expiration;
}
