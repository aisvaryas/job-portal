package com.example.demo.Service.resume;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.resume.Skill;
import com.example.demo.Repository.resume.SkillRepo;
import com.example.demo.dto.resume.SkillRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepo skillRepository;
    //main skills table (add skills n view for suggestion we can use.)
    //no other operations to be done here for user specific seekerskilltable
    public Skill addSkill(SkillRequest request) {
        Skill skill = skillRepository
                .findBySkillNameIgnoreCase(request.getSkillName())
                .orElse(null);

        if(skill == null) {
            skill = Skill.builder()
                    .skillName(request.getSkillName())
                    .build();

            skillRepository.save(skill);
        }
        return skill;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}