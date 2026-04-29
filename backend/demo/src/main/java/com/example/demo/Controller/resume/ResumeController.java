package com.example.demo.Controller.resume;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.resume.Resume;
import com.example.demo.Service.resume.ResumeService;
import com.example.demo.response.Apiresponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Apiresponse<Resume>> upload(
            Authentication auth,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value="isDefault", defaultValue="false") boolean isDefault,
            @RequestParam(value="visibility", defaultValue="PRIVATE") String visibility) {

        String email = auth.getName();

        Resume resume = resumeService.uploadResume(email, file, isDefault, visibility);
        System.out.println("Logged in user: " + auth.getName());
        System.out.println("File name: " + file.getOriginalFilename());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("Resume uploaded successfully", 201, resume));
    }

    @GetMapping("/all")
    public ResponseEntity<Apiresponse<List<Resume>>> getAll(Authentication auth) {
        String email = auth.getName();

        return ResponseEntity.ok(
            new Apiresponse<>("Resumes fetched successfully", 200,
                resumeService.getResumes(email))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiresponse<String>> getResume(@PathVariable Long id) {
        return ResponseEntity.ok(
            new Apiresponse<>("Resume URL generated", 200,
                resumeService.getResumePath(id))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Apiresponse<String>> delete(
            Authentication auth,
            @PathVariable Long id) {

        String email = auth.getName();

        String msg = resumeService.deleteResume(email, id);

        return ResponseEntity.ok(
            new Apiresponse<>(msg, 200, msg)
        );
    }
}