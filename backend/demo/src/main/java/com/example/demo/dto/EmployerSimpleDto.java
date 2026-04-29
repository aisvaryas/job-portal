package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerSimpleDto {
    private Long id;
    private String companyName;
    private String industry;
    private String website;
}
