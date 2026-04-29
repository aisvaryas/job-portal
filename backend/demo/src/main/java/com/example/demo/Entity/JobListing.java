package com.example.demo.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String title;
    @NotBlank
    @Column(nullable = false, length = 3000)
    private String description;

    @Column(length = 2000)
    private String requirements;

    @Column(length = 100)
    private String location;

    @Column(length = 30)
    private String jobType;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime postedDate;

    private LocalDateTime deadline;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(length = 100)
    private String experienceLevel;

    @Column(length = 500)
    private String benefits;

    @Column(length = 500)
    private String skillsRequired;

    @Column(length = 1000)
    private String aboutCompany;
    
}
