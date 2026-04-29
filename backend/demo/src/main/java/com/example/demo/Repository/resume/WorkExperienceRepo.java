package com.example.demo.Repository.resume;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.WorkExperience;

public interface WorkExperienceRepo extends JpaRepository<WorkExperience, Long> {

    List<WorkExperience> findByJobSeeker(Jobseeker jobSeeker);
    
}
