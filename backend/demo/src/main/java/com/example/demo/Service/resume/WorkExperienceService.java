package com.example.demo.Service.resume;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.WorkExperience;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.resume.WorkExperienceRepo;
import com.example.demo.dto.resume.WorkExperienceRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final WorkExperienceRepo workExperienceRepository;
    private final JobSeekerRepo jobSeekerRepository;

    @Transactional
    public WorkExperience addExperience(String userEmail, WorkExperienceRequest request) {
        // Validate input
        if (request == null || request.getCompany() == null || request.getCompany().trim().isEmpty()) {
            throw new BusinessException("Company name is required");
        }
        if (request.getPosition() == null || request.getPosition().trim().isEmpty()) {
            throw new BusinessException("Position is required");
        }
        if (request.getStartDate() == null) {
            throw new BusinessException("Start date is required");
        }

        Jobseeker jobSeeker = jobSeekerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        // Validate date logic
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("End date cannot be before start date");
        }

        WorkExperience exp = WorkExperience.builder()
                .jobSeeker(jobSeeker)
                .company(request.getCompany().trim())
                .position(request.getPosition().trim())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation() != null ? request.getLocation().trim() : null)
                .description(request.getDescription())
                .build();

        return workExperienceRepository.save(exp);
    }

    public List<WorkExperience> getAllExperience(String userEmail) {
        Jobseeker jobSeeker = jobSeekerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));
        
        return workExperienceRepository.findByJobSeeker(jobSeeker);
    }

    @Transactional
    public WorkExperience updateWorkExperience(String userEmail, Long id, WorkExperienceRequest request) {
        // Validate input
        if (request == null || request.getCompany() == null || request.getCompany().trim().isEmpty()) {
            throw new BusinessException("Company name is required");
        }
        if (request.getPosition() == null || request.getPosition().trim().isEmpty()) {
            throw new BusinessException("Position is required");
        }
        if (request.getStartDate() == null) {
            throw new BusinessException("Start date is required");
        }

        Jobseeker jobSeeker = jobSeekerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        WorkExperience work = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work experience not found with id: " + id));

        // Verify ownership - ensure this experience belongs to the user
        if (!work.getJobSeeker().getId().equals(jobSeeker.getId())) {
            throw new BusinessException("You are not authorized to update this work experience");
        }

        // Validate date logic
        if (request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException("End date cannot be before start date");
        }

        work.setCompany(request.getCompany().trim());
        work.setPosition(request.getPosition().trim());
        work.setStartDate(request.getStartDate());
        work.setEndDate(request.getEndDate());
        work.setLocation(request.getLocation() != null ? request.getLocation().trim() : null);
        work.setDescription(request.getDescription());

        return workExperienceRepository.save(work);
    }

    @Transactional
    public String deleteWorkExperience(String userEmail, Long id) {
        Jobseeker jobSeeker = jobSeekerRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        WorkExperience work = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work experience not found with id: " + id));

        // Verify ownership
        if (!work.getJobSeeker().getId().equals(jobSeeker.getId())) {
            throw new BusinessException("You are not authorized to delete this work experience");
        }

        workExperienceRepository.delete(work);
        return "Work Experience Deleted Successfully";
    }
}