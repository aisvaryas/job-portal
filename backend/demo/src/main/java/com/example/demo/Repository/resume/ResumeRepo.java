package com.example.demo.Repository.resume;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.Resume;

public interface ResumeRepo extends JpaRepository<Resume, Long> {
    List<Resume> findByJobseekerOrderByIdDesc(Jobseeker jobseeker);
}
