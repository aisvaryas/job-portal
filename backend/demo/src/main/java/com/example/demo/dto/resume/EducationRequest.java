package com.example.demo.dto.resume;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EducationRequest {
    @NotBlank
    private String institution;
    @NotBlank
    private String degree;
    private String fieldOfStudy;

    private String description;
    @NotNull
    private LocalDate graduationDate;
    private String grade;
}
