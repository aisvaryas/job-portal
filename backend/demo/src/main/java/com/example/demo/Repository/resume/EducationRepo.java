package com.example.demo.Repository.resume;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.Education;

public interface EducationRepo extends JpaRepository<Education, Long>{
    List<Education> findByJobseeker(Jobseeker jobseeker);
    Optional<Education> findByIdAndJobseeker(Long id, Jobseeker jobseeker);
}
