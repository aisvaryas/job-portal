package com.example.demo.mapper;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.dto.UserRegisterdto;
import com.example.demo.dto.UserResponsedto;

public class UserMapper {

    public static User toEntity(UserRegisterdto dto) {

        User user = new User();

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        String role = dto.getRole().toUpperCase();
        if ("SEEKER".equals(role)) {
            role = "JOB_SEEKER";
        }
        user.setRole(Role.valueOf(role));
        user.setIsActive(true);

        return user;
    }

    public static UserResponsedto toDto(User user) {

        UserResponsedto dto = new UserResponsedto();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole() == Role.JOB_SEEKER ? Role.JOB_SEEKER.name() : user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setIsActive(user.getIsActive());

        return dto;
    }
}
