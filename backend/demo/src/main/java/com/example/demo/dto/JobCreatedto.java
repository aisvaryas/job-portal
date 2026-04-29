package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCreatedto {
    private String title;
    private String description;
    private String location;
    private String jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String requirements;
    private LocalDateTime deadline;
    private String experienceLevel;
    private String benefits;
    private String skillsRequired;
    private String aboutCompany;
}
