package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.JobApplication;
import com.example.demo.Service.JobApplicationService;
import com.example.demo.dto.ApplicationRequestdto;
import com.example.demo.dto.ApplicationResponsedto;
import com.example.demo.response.Apiresponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService applicationService;

    @PostMapping("/apply/{email}/{jobId}")
    public ResponseEntity<Apiresponse<JobApplication>> apply(
            @PathVariable String email,
            @PathVariable Long jobId,
            @Valid @RequestBody ApplicationRequestdto dto) {
        JobApplication app = applicationService.apply(email, jobId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("Application submitted successfully", 201, app));
    }

    @GetMapping("/my/{email}")
    public ResponseEntity<Apiresponse<List<ApplicationResponsedto>>> myApplications(@PathVariable String email) {
        return ResponseEntity.ok(new Apiresponse<>(
                "Applications fetched successfully",
                200,
                applicationService.myApplicationDtos(email)));
    }

    @GetMapping("/my")
    public ResponseEntity<Apiresponse<List<ApplicationResponsedto>>> myApplications(Authentication authentication) {
        return ResponseEntity.ok(new Apiresponse<>(
                "Applications fetched successfully",
                200,
                applicationService.myApplicationDtos(authentication.getName())));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<Apiresponse<ApplicationResponsedto>> getApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(new Apiresponse<>(
                "Application fetched successfully",
                200,
                applicationService.getApplicationDtoById(applicationId)));
    }
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<Apiresponse<List<ApplicationResponsedto>>> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(new Apiresponse<>(
                "Job applications fetched successfully",
                200,
                applicationService.getApplicationDtosByJob(jobId)));
    }
    
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Apiresponse<ApplicationResponsedto>> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        ApplicationResponsedto app = applicationService.updateApplicationStatusDto(applicationId, status);
        return ResponseEntity.ok(new Apiresponse<>(
                "Application status updated successfully",
                200,
                app));
    }
}
