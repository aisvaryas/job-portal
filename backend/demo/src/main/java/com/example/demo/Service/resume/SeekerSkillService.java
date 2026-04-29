package com.example.demo.Service.resume;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.SeekerSkill;
import com.example.demo.Entity.resume.Skill;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.resume.SeekerSkillRepo;
import com.example.demo.Repository.resume.SkillRepo;
import com.example.demo.dto.resume.SkillRequest;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeekerSkillService {

    private final JobSeekerRepo jobSeekerRepo;
    private final SkillRepo skillRepository;
    private final SeekerSkillRepo seekerSkillRepository;

    @Transactional
    public SeekerSkill addSkill(String userEmail, SkillRequest request) {
        // Validate input
        if (request == null || request.getSkillName() == null || request.getSkillName().trim().isEmpty()) {
            throw new BusinessException("Skill name cannot be empty");
        }

        Jobseeker jobseeker = jobSeekerRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        String skillName = request.getSkillName().trim();
        
        // Check if skill already exists for this user
        Skill existingSkill = skillRepository.findBySkillNameIgnoreCase(skillName).orElse(null);
        boolean skillAlreadyAdded = existingSkill != null
                && seekerSkillRepository.findByJobseekerAndSkill(jobseeker, existingSkill).isPresent();
        
        if (skillAlreadyAdded) {
            throw new BusinessException("Skill already added to your profile");
        }

        Skill skill = existingSkill;
        if (skill == null) {
            skill = Skill.builder()
                    .skillName(skillName)
                    .build();
            skill = skillRepository.save(skill);
        }
        
        SeekerSkill seekerSkill = SeekerSkill.builder()
                .jobseeker(jobseeker)
                .skill(skill)
                .build();
        return seekerSkillRepository.save(seekerSkill);
    }

    @Transactional
    public SeekerSkill updateSkill(String userEmail, String oldSkillName, SkillRequest request) {
        if (request == null || request.getSkillName() == null || request.getSkillName().trim().isEmpty()) {
            throw new BusinessException("New skill name cannot be empty");
        }

        Jobseeker jobSeeker = jobSeekerRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        Skill oldSkill = skillRepository.findBySkillNameIgnoreCase(oldSkillName)
                .orElseThrow(() -> new ResourceNotFoundException("Old skill not found: " + oldSkillName));

        SeekerSkill seekerSkill = seekerSkillRepository.findByJobseekerAndSkill(jobSeeker, oldSkill)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found for this user"));

        String newSkillName = request.getSkillName().trim();
        Skill newSkill = skillRepository.findBySkillNameIgnoreCase(newSkillName).orElse(null);
        if (newSkill != null && seekerSkillRepository.findByJobseekerAndSkill(jobSeeker, newSkill).isPresent()) {
            throw new BusinessException("Skill already added to your profile");
        }
        
        if (newSkill == null) {
            newSkill = Skill.builder()
                    .skillName(newSkillName)
                    .build();
            newSkill = skillRepository.save(newSkill);
        }

        seekerSkill.setSkill(newSkill);
        return seekerSkillRepository.save(seekerSkill);
    }

    @Transactional
    public String deleteSkill(String userEmail, String skillName) {
        Jobseeker jobSeeker = jobSeekerRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        Skill skill = skillRepository.findBySkillNameIgnoreCase(skillName)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + skillName));

        SeekerSkill seekerSkill = seekerSkillRepository.findByJobseekerAndSkill(jobSeeker, skill)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found for this user"));

        seekerSkillRepository.delete(seekerSkill);
        return "Skill Deleted Successfully";
    }

    public List<SeekerSkill> getAllSkills(String userEmail) {
        Jobseeker jobSeeker = jobSeekerRepo.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + userEmail));

        return seekerSkillRepository.findByJobseeker(jobSeeker);
    }
}
