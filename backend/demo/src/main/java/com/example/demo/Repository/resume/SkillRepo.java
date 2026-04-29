package com.example.demo.Repository.resume;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.resume.Skill;
import java.util.Optional;
public interface SkillRepo extends JpaRepository<Skill, Long> {
     Optional<Skill> findBySkillNameIgnoreCase(String skillName);
    
}
