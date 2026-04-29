package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequestdto {
    @NotNull
    private Long resumeId;
    private String coverLetter;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    @Email
    private String email;
}
