package com.example.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepo;
import com.example.demo.dto.UserRegisterdto;
import com.example.demo.dto.UserUpdatedto;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserRegisterdto dto) {
        if (repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("Email already registered");
        }

        Role role = normalizeRole(dto.getRole());
        User user = UserMapper.toEntity(dto);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return repo.save(user);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User getUserById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }

    public User updateUser(Long id, UserUpdatedto dto) {

        User user = repo.findById(id).orElse(null);

        if(user != null) {
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            return repo.save(user);
        }

        return null;
    }

    public String deleteUser(Long id) {

        User user = repo.findById(id).orElse(null);

        if (user != null) {
            user.setIsActive(false);
            repo.save(user);
            return "User Deactivated Successfully";
        }

        return "User Not Found";
    }
    public String reactivateUser(Long id) {

        User user = repo.findById(id).orElse(null);

        if(user != null) {
            user.setIsActive(true);
            repo.save(user);
            return "User Reactivated Successfully";
        }

        return "User Not Found";
    }

    private Role normalizeRole(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            throw new BusinessException("Role is required");
        }
        String role = rawRole.trim().toUpperCase();
        if ("SEEKER".equals(role)) {
            role = "JOB_SEEKER";
        }
        try {
            return Role.valueOf(role);
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Invalid role. Allowed roles: JOB_SEEKER, EMPLOYER");
        }
    }
}