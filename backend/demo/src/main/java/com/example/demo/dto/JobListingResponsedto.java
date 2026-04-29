package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingResponsedto {

    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String companyName;
    private String location;
    private String jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String status;
    private String experienceLevel;
    private String benefits;
    private String skillsRequired;
    private String aboutCompany;
    private EmployerSimpleDto employer;
    private LocalDateTime postedDate;
    private LocalDateTime deadline;
    private Long applicantCount;
}
