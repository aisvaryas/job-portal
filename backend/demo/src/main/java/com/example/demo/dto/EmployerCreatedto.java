package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerCreatedto {
    private Long userId;
    private String contactFirstName;
    private String contactLastName;
    private String companyName;
    private String industry;
    private String website;
    private String description;
    private String phone;
    private String location;

}
