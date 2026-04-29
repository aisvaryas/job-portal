package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerResponsedto {

    private Long id;
    private String contactFirstName;
    private String contactLastName;
    private String companyName;
    private String industry;
    private String website;
    private String description;
    private String phone;
    private String location;
    private String verifiedStatus;
    private Boolean isActive;
}