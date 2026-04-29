package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.JobListing;

public interface JobListingRepo extends JpaRepository<JobListing,Long> {
    List<JobListing> findByLocation(String location);

    List<JobListing> findByTitle(String title);
    List<JobListing> findByTitleAndLocation(String title,String location);
    
    List<JobListing> findByStatus(String status);
    
    List<JobListing> findByLocationContainingIgnoreCaseAndStatus(String location, String status);
    
    List<JobListing> findByTitleContainingIgnoreCaseAndStatus(String title, String status);
    
    List<JobListing> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndStatus(String title, String location, String status);
    
    List<JobListing> findByExperienceLevelIgnoreCaseAndStatus(String experienceLevel, String status);
    
    List<JobListing> findByLocationContainingIgnoreCaseAndExperienceLevelIgnoreCaseAndStatus(String location, String experienceLevel, String status);
    
    List<JobListing> findByEmployerId(Long employerId);
}
