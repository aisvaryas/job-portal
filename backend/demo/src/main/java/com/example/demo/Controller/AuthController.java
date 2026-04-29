package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Service.JobseekerService;
import com.example.demo.Service.UserService;
import com.example.demo.dto.Logindto;
import com.example.demo.dto.LoginResponsedto;
import com.example.demo.exception.BusinessException;
import com.example.demo.response.Apiresponse;
import com.example.demo.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JobseekerService jobseekerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Apiresponse<LoginResponsedto>> login(@Valid @RequestBody Logindto dto) {
        User user = userService.getUserByEmail(dto.getEmail());

        if (user == null || !Boolean.TRUE.equals(user.getIsActive())) {
            throw new BusinessException("Invalid credentials");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        Role normalizedRole = user.getRole() == Role.JOB_SEEKER ? Role.JOB_SEEKER : user.getRole();
        
        // Ensure jobseeker profile exists if role is JOB_SEEKER
        if (normalizedRole == Role.JOB_SEEKER) {
            jobseekerService.ensureJobseekerExists(user);
        }
        
        String token = jwtService.generateToken(user.getId(), user.getEmail(), normalizedRole);

        LoginResponsedto response = new LoginResponsedto(
                token,
                user.getId(),
                user.getEmail(),
                normalizedRole.name(),
                System.currentTimeMillis() + jwtService.getExpirationMs());

        return ResponseEntity.ok(new Apiresponse<>("Login successful", 200, response));
    }
}
