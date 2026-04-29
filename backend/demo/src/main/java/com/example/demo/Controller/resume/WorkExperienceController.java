package com.example.demo.Controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.resume.WorkExperience;
import com.example.demo.Service.resume.WorkExperienceService;
import com.example.demo.dto.resume.WorkExperienceRequest;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/experience")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService service;

    @PostMapping("/save/{email}")
    public ResponseEntity<Apiresponse<WorkExperience>> save( @PathVariable String email,@RequestBody WorkExperienceRequest request) {
        WorkExperience exp = service.addExperience(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Apiresponse<>("Work Experience Saved Successfully", 201, exp));
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<Apiresponse<List<WorkExperience>>> getAll(@PathVariable String email) {
        List<WorkExperience> list = service.getAllExperience(email);
        return ResponseEntity.ok(new Apiresponse<>("Work Experience Fetched Successfully", 200, list));
    }

    @PutMapping("/update/{email}/{id}")
    public ResponseEntity<Apiresponse<WorkExperience>> update(@PathVariable String email,@PathVariable Long id,@RequestBody WorkExperienceRequest request) {
        WorkExperience exp = service.updateWorkExperience(email, id, request);
        return ResponseEntity.ok(new Apiresponse<>("Work Experience Updated Successfully", 200, exp));
    }

    @DeleteMapping("/delete/{email}/{id}")
    public ResponseEntity<Apiresponse<String>> delete(@PathVariable String email,@PathVariable Long id) {
        String msg = service.deleteWorkExperience(email, id);
        return ResponseEntity.ok(new Apiresponse<>(msg, 200, msg));
    }
}