package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.JobApplication;
import com.example.demo.Entity.JobListing;
import com.example.demo.Entity.Jobseeker;

public interface JobApplicationRepo extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobseekerOrderByAppliedAtDesc(Jobseeker jobseeker);
    
    List<JobApplication> findByJobOrderByAppliedAtDesc(JobListing job);
    
    List<JobApplication> findByJobId(Long jobId);
    
    long countByJob(JobListing job);
}
