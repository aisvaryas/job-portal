package com.example.demo.mapper;

import java.util.List;

import com.example.demo.Entity.JobApplication;
import com.example.demo.Entity.JobListing;
import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.SeekerSkill;
import com.example.demo.Entity.resume.WorkExperience;
import com.example.demo.dto.ApplicationResponsedto;

public class ApplicationMapper {

    public static ApplicationResponsedto toDto(
            JobApplication application,
            List<SeekerSkill> skills,
            List<WorkExperience> experiences) {

        JobListing job = application.getJob();
        Jobseeker seeker = application.getJobseeker();

        ApplicationResponsedto dto = new ApplicationResponsedto();
        dto.setId(application.getId());
        dto.setJobId(job.getId());
        dto.setJobTitle(job.getTitle());
        dto.setCompanyName(job.getEmployer().getCompanyName());
        dto.setLocation(job.getLocation());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setJobType(job.getJobType());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setDescription(job.getDescription());
        dto.setRequirements(job.getRequirements());
        dto.setBenefits(job.getBenefits());
        dto.setAboutCompany(job.getAboutCompany());
        dto.setSkillsRequired(job.getSkillsRequired());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setEmail(application.getEmail());
        dto.setPhoneNumber(application.getPhoneNumber());

        dto.setSeekerId(seeker.getId());
        dto.setApplicantName((seeker.getFirstName() + " " + seeker.getLastName()).trim());
        dto.setSeekerEmail(seeker.getUser().getEmail());
        dto.setSeekerPhone(seeker.getPhone());
        dto.setSeekerLocation(seeker.getLocation());
        dto.setSeekerHeadline(seeker.getHeadline());

        if (application.getResume() != null) {
            dto.setResumeId(application.getResume().getId());
            dto.setResumeFileName(application.getResume().getFileName());
            dto.setResumeFileUrl(application.getResume().getFileURL());
        }

        dto.setSkills(skills.stream()
                .map(skill -> skill.getSkill().getSkillName())
                .toList());
        dto.setExperience(experiences.stream()
                .map(exp -> "%s at %s%s".formatted(
                        exp.getPosition(),
                        exp.getCompany(),
                        exp.getLocation() == null || exp.getLocation().isBlank()
                                ? ""
                                : " - " + exp.getLocation()))
                .toList());

        return dto;
    }
}
