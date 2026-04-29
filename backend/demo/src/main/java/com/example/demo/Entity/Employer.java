package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank(message="First name required")
    @Column(name = "contactFirstName")
    private String contactFirstName;

    @NotBlank(message="Last name required")
    @Column(name = "contactLastName")
    private String contactLastName;

    @Column(name = "companyName", nullable = false)
    private String companyName;

    @Column(name = "industry")
    private String industry;

    @Column(name = "website")
    private String website;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "phone")
    private String phone;

    @Column(name = "location")
    private String location;

    @Column(name = "verifiedStatus", nullable = false)
    private String verifiedStatus;

    @Column(nullable = false)
    private Boolean isActive = true;

       
    
    
}
