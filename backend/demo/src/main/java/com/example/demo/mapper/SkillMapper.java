package com.example.demo.mapper;

import com.example.demo.Entity.resume.SeekerSkill;
import com.example.demo.dto.resume.SkillResponse;

public class SkillMapper {
    
    public static SkillResponse toResponse(SeekerSkill seekerSkill) {
        if (seekerSkill == null || seekerSkill.getSkill() == null) {
            return null;
        }
        return new SkillResponse(
            seekerSkill.getSkill().getId(),
            seekerSkill.getSkill().getSkillName()
        );
    }
    
    public static SkillResponse toSimpleResponse(SeekerSkill seekerSkill) {
        if (seekerSkill == null || seekerSkill.getSkill() == null) {
            return null;
        }
        return new SkillResponse(
            seekerSkill.getId(),
            seekerSkill.getSkill().getSkillName()
        );
    }
}
