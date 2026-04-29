package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jobseeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message="First name required")
    @Column(name = "firstName", nullable = false, length=50)
    private String firstName;

    @NotBlank(message="Last name required")
    @Column(name = "lastName", nullable = false, length=50)
    private String lastName;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter valid phone number")
    @Column(unique = true, nullable = true)
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "headline")
    private String headline;

    @Column(length = 2000)
    private String summary;

    private boolean isActive;
    
   

    
    
}
