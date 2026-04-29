package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponsedto {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String jobType;
    private String experienceLevel;
    private String description;
    private String requirements;
    private String benefits;
    private String aboutCompany;
    private String skillsRequired;
    private String status;
    private LocalDateTime appliedAt;
    private String coverLetter;
    private String email;
    private String phoneNumber;
    private Long seekerId;
    private String applicantName;
    private String seekerEmail;
    private String seekerPhone;
    private String seekerLocation;
    private String seekerHeadline;
    private Long resumeId;
    private String resumeFileName;
    private String resumeFileUrl;
    private List<String> skills;
    private List<String> experience;
}
