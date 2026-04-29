package com.example.demo.dto.resume;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillRequest {
    @NotBlank
    private String skillName;
}
