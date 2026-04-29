package com.example.demo.dto.resume;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkExperienceRequest {
    @NotBlank
    private String company;
    @NotBlank
    private String position;
    @NotNull
    @PastOrPresent
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String description;
}
