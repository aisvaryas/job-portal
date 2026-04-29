package com.example.demo.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.JobListing;
import com.example.demo.Service.JobListingService;
import com.example.demo.dto.JobListingResponsedto;
import com.example.demo.mapper.JobListingMapper;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobListingController {

    private final JobListingService service;

    @GetMapping
    public ResponseEntity<Apiresponse<List<JobListingResponsedto>>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experience) {

        List<JobListingResponsedto> jobs = service.searchOpenJobs(title, location, experience)
                .stream()
                .map(JobListingMapper::toDto)
                .toList();

        return ResponseEntity.ok(new Apiresponse<>("Jobs fetched successfully", 200, jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiresponse<JobListingResponsedto>> byId(@PathVariable Long id) {
        JobListing job = service.getById(id);
        if (job == null) {
            throw new com.example.demo.exception.ResourceNotFoundException("Job Not Found");
        }
        return ResponseEntity.ok(new Apiresponse<>("Job Found", 200, JobListingMapper.toDto(job)));
    }
}
