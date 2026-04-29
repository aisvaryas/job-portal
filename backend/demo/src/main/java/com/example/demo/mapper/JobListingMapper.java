package com.example.demo.mapper;

import com.example.demo.Entity.Employer;
import com.example.demo.Entity.JobListing;
import com.example.demo.dto.EmployerSimpleDto;
import com.example.demo.dto.JobCreatedto;
import com.example.demo.dto.JobListingResponsedto;

public class JobListingMapper {

    public static JobListing toEntity(JobCreatedto dto, Employer employer) {

        JobListing job = new JobListing();

        job.setEmployer(employer);
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
        job.setStatus("OPEN");

        return job;
    }
    
    public static JobListingResponsedto toDto(JobListing job) {
        EmployerSimpleDto employerDto = new EmployerSimpleDto(
            job.getEmployer().getId(),
            job.getEmployer().getCompanyName(),
            job.getEmployer().getIndustry(),
            job.getEmployer().getWebsite()
        );

        return new JobListingResponsedto(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequirements(),
                job.getEmployer().getCompanyName(),
                job.getLocation(),
                job.getJobType(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getStatus(),
                job.getExperienceLevel(),
                job.getBenefits(),
                job.getSkillsRequired(),
                job.getAboutCompany(),
                employerDto,
                job.getPostedDate(),
                job.getDeadline(),
                0L
        );
    }   
}
