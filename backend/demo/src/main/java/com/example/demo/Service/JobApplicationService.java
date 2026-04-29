package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.JobApplication;
import com.example.demo.Entity.JobListing;
import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.Resume;
import com.example.demo.Repository.JobApplicationRepo;
import com.example.demo.Repository.JobListingRepo;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.resume.SeekerSkillRepo;
import com.example.demo.Repository.resume.ResumeRepo;
import com.example.demo.Repository.resume.WorkExperienceRepo;
import com.example.demo.dto.ApplicationRequestdto;
import com.example.demo.dto.ApplicationResponsedto;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ApplicationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepo applicationRepo;
    private final JobSeekerRepo jobSeekerRepo;
    private final JobListingRepo jobListingRepo;
    private final ResumeRepo resumeRepo;
    private final SeekerSkillRepo seekerSkillRepo;
    private final WorkExperienceRepo workExperienceRepo;
    private final NotificationService notificationService;

    public JobApplication apply(String email, Long jobId, ApplicationRequestdto dto) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        JobListing job = jobListingRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!"OPEN".equalsIgnoreCase(job.getStatus())) {
            throw new BusinessException("This job is closed and no longer accepts applications");
        }
        Resume resume = resumeRepo.findById(dto.getResumeId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getJobseeker().getId().equals(seeker.getId())) {
            throw new BusinessException("Selected resume does not belong to current seeker");
        }

        JobApplication app = new JobApplication();
        app.setJob(job);
        app.setJobseeker(seeker);
        app.setResume(resume);
        app.setCoverLetter(dto.getCoverLetter());
        app.setPhoneNumber(dto.getPhoneNumber());
        app.setEmail(dto.getEmail());
        app.setStatus("APPLIED");
        
        JobApplication savedApp = applicationRepo.save(app);
        
        // Send notification to employer
        String notificationMessage = String.format("New applicant %s %s applied for %s role.",
            seeker.getFirstName(), seeker.getLastName(), job.getTitle());
        notificationService.createNotification(
            job.getEmployer().getUser(),
            notificationMessage,
            "NEW_APPLICATION",
            "APPLICATION",
            savedApp.getId()
        );
        
        return savedApp;
    }

    public List<JobApplication> myApplications(String email) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found"));
        return applicationRepo.findByJobseekerOrderByAppliedAtDesc(seeker);
    }

    public List<ApplicationResponsedto> myApplicationDtos(String email) {
        return myApplications(email).stream()
                .map(this::toDto)
                .toList();
    }
    
    public List<JobApplication> getApplicationsByJob(Long jobId) {
        JobListing job = jobListingRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return applicationRepo.findByJobOrderByAppliedAtDesc(job);
    }

    public List<ApplicationResponsedto> getApplicationDtosByJob(Long jobId) {
        return getApplicationsByJob(jobId).stream()
                .map(this::toDto)
                .toList();
    }

    public ApplicationResponsedto getApplicationDtoById(Long applicationId) {
        return toDto(applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found")));
    }
    
    public long getApplicationCountByJob(Long jobId) {
        JobListing job = jobListingRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return applicationRepo.countByJob(job);
    }
    
    public JobApplication updateApplicationStatus(Long applicationId, String newStatus) {
        JobApplication app = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        
        String normalizedStatus = normalizeStatus(newStatus);
        app.setStatus(normalizedStatus);
        
        JobApplication updatedApp = applicationRepo.save(app);
        
        // Send notification to seeker
        String notificationMessage;
        if ("SHORTLISTED".equals(normalizedStatus)) {
            notificationMessage = String.format("Congratulations! You have been shortlisted for %s at %s.",
                app.getJob().getTitle(), app.getJob().getEmployer().getCompanyName());
        } else if ("REJECTED".equals(normalizedStatus)) {
            notificationMessage = String.format("Unfortunately, your application for %s at %s has been rejected.",
                app.getJob().getTitle(), app.getJob().getEmployer().getCompanyName());
        } else if ("HIRED".equals(normalizedStatus)) {
            notificationMessage = String.format("Great news! You have been selected for %s at %s.",
                app.getJob().getTitle(), app.getJob().getEmployer().getCompanyName());
        } else {
            notificationMessage = String.format("Your application status for %s has been updated to %s.",
                app.getJob().getTitle(), normalizedStatus);
        }
        
        notificationService.createNotification(
            app.getJobseeker().getUser(),
            notificationMessage,
            normalizedStatus,
            "APPLICATION",
            applicationId
        );
        
        return updatedApp;
    }

    public ApplicationResponsedto updateApplicationStatusDto(Long applicationId, String newStatus) {
        return toDto(updateApplicationStatus(applicationId, newStatus));
    }

    private ApplicationResponsedto toDto(JobApplication application) {
        return ApplicationMapper.toDto(
                application,
                seekerSkillRepo.findByJobseeker(application.getJobseeker()),
                workExperienceRepo.findByJobSeeker(application.getJobseeker()));
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BusinessException("Application status is required");
        }

        return switch (status.trim().toUpperCase()) {
            case "APPLIED", "SHORTLISTED", "REJECTED", "HIRED", "INTERVIEW", "UNDER_REVIEW" -> status.trim().toUpperCase();
            case "ACCEPTED" -> "HIRED";
            case "REVIEWING" -> "UNDER_REVIEW";
            default -> throw new BusinessException("Unsupported application status: " + status);
        };
    }
}
