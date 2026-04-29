package com.example.demo.Repository.resume;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.SeekerSkill;
import com.example.demo.Entity.resume.Skill;

public interface SeekerSkillRepo  extends JpaRepository<SeekerSkill, Long>{
     List<SeekerSkill> findByJobseeker(Jobseeker jobseeker);
     List<SeekerSkill> findByJobseekerId(Long seekerId);

     Optional<SeekerSkill> findByJobseekerAndSkill(Jobseeker seeker, Skill skill);
}
