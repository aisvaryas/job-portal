package com.example.demo.Service;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Employer;
import com.example.demo.Entity.JobListing;
import com.example.demo.Repository.JobListingRepo;
import com.example.demo.dto.JobCreatedto;
import com.example.demo.mapper.JobListingMapper;

@Service
public class JobListingService {

    @Autowired
    private JobListingRepo repo;

    public JobListing save(JobCreatedto dto, Employer employer) {

        JobListing job = JobListingMapper.toEntity(dto, employer);
        return repo.save(job);
    }

    public List<JobListing> getAll() {
        return repo.findByStatus("OPEN");
    }
    
    public List<JobListing> getAllJobs() {
        return repo.findAll();
    }

    public JobListing getById(Long id) {
        return repo.findById(id).orElse(null);
    }
    
    public JobListing updateJob(Long id, JobCreatedto dto) {

        JobListing job = repo.findById(id).orElse(null);
        if(job != null) {
            job.setTitle(dto.getTitle());
            job.setDescription(dto.getDescription());
            job.setRequirements(dto.getRequirements());
            job.setLocation(dto.getLocation());
            job.setJobType(dto.getJobType());
            job.setSalaryMin(dto.getSalaryMin());
            job.setSalaryMax(dto.getSalaryMax());
            job.setDeadline(dto.getDeadline());
            job.setExperienceLevel(dto.getExperienceLevel());
            job.setBenefits(dto.getBenefits());
            job.setSkillsRequired(dto.getSkillsRequired());
            job.setAboutCompany(dto.getAboutCompany());
            return repo.save(job);
        }
        return null;
    }

    public String closeJob(Long id) {

        JobListing job = repo.findById(id).orElse(null);

        if(job != null) {
            job.setStatus("CLOSED");
            repo.save(job);
            return "Job Closed Successfully";
        }

        return "Job Not Found";
    }
    
    public String reopenJob(Long id) {
        JobListing job = repo.findById(id).orElse(null);
        if(job != null) {
            job.setStatus("OPEN");
            repo.save(job);
            return "Job Reopened Successfully";
        }
        return "Job Not Found";
    }

    public String deleteJob(Long id) {
        try {
            repo.deleteById(id);
            return "Job Deleted Successfully";
        } catch (Exception e) {
            return "Failed to delete job";
        }
    }

    public List<JobListing> searchByLocation(String location) {
        return repo.findByLocationContainingIgnoreCaseAndStatus(location, "OPEN");
    }

    public List<JobListing> searchByTitle(String title) {
        return repo.findByTitleContainingIgnoreCaseAndStatus(title, "OPEN");
    }

    public List<JobListing> searchByTitleAndLocation(String title, String location) {
        return repo.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndStatus(title, location, "OPEN");
    }
    
    public List<JobListing> searchByExperienceLevel(String experienceLevel) {
        return repo.findByExperienceLevelIgnoreCaseAndStatus(normalizeExperienceLevel(experienceLevel), "OPEN");
    }
    
    public List<JobListing> searchByLocationAndExperience(String location, String experienceLevel) {
        return repo.findByLocationContainingIgnoreCaseAndExperienceLevelIgnoreCaseAndStatus(
                location,
                normalizeExperienceLevel(experienceLevel),
                "OPEN");
    }
    
    public List<JobListing> getJobsByEmployer(Long employerId) {
        return repo.findByEmployerId(employerId);
    }

    public List<JobListing> searchOpenJobs(String title, String location, String experience) {
        String normalizedExperience = normalizeExperienceLevel(experience);

        return repo.findByStatus("OPEN").stream()
                .filter(job -> title == null || title.isBlank()
                        || containsIgnoreCase(job.getTitle(), title)
                        || containsIgnoreCase(job.getEmployer().getCompanyName(), title)
                        || containsIgnoreCase(job.getSkillsRequired(), title))
                .filter(job -> location == null || location.isBlank()
                        || containsIgnoreCase(job.getLocation(), location))
                .filter(job -> normalizedExperience == null
                        || normalizedExperience.equalsIgnoreCase(job.getExperienceLevel()))
                .toList();
    }

    private boolean containsIgnoreCase(String source, String term) {
        return source != null
                && term != null
                && source.toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT));
    }

    private String normalizeExperienceLevel(String experience) {
        if (experience == null || experience.isBlank()) {
            return null;
        }

        String value = experience.trim().toLowerCase(Locale.ROOT);
        return switch (value) {
            case "entry", "entry level", "entry_level", "0", "1" -> "ENTRY";
            case "mid", "mid level", "mid_level", "middle", "2", "3", "4" -> "MID";
            case "senior", "senior level", "senior_level", "5", "6", "7", "8", "9", "10" -> "SENIOR";
            default -> experience.trim().toUpperCase(Locale.ROOT);
        };
    }
}
