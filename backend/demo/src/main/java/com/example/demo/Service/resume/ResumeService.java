package com.example.demo.Service.resume;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Jobseeker;
import com.example.demo.Entity.resume.Resume;
import com.example.demo.Repository.JobSeekerRepo;
import com.example.demo.Repository.resume.ResumeRepo;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepo resumeRepo;
    private final JobSeekerRepo jobSeekerRepo;
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;

    @Transactional
    public Resume uploadResume(String email, MultipartFile file, boolean isDefault, String visibility) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("Email cannot be empty");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Resume file cannot be empty");
        }

        try {
            Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));

            ensureBucket();

            String extension = getExtension(file.getOriginalFilename());
            Resume.FileType fileType = parseFileType(extension);
            String objectName = "resumes/" + email + "/" + UUID.randomUUID() + "." + extension;

            try (InputStream stream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(stream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }

            Resume resume = Resume.builder()
                    .jobseeker(seeker)
                    .fileName(file.getOriginalFilename())
                    .fileType(fileType)
                    .fileURL(minioUrl + "/" + bucketName + "/" + objectName)
                    .isDefault(isDefault)
                    .visibility(visibility == null || visibility.trim().isEmpty() ? "PRIVATE" : visibility)
                    .build();
            return resumeRepo.save(resume);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException("Failed to upload resume: " + ex.getMessage());
        }
    }

    public List<Resume> getResumes(String email) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));
        return resumeRepo.findByJobseekerOrderByIdDesc(seeker);
    }

    public Resume getResumeById(Long resumeId) {
        return resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    public String getResumePath(Long resumeId) {
        try {
            Resume resume = getResumeById(resumeId);
            String objectName = extractObjectName(resume.getFileURL());
            if (objectName == null || objectName.isBlank()) {
                throw new BusinessException("Invalid resume file path");
            }
            
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(24, TimeUnit.HOURS)
                            .build());
            return presignedUrl;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new BusinessException("Failed to generate resume download link: " + ex.getMessage());
        }
    }

    @Transactional
    public String deleteResume(String email, Long resumeId) {
        Jobseeker seeker = jobSeekerRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Job seeker not found with email: " + email));

        Resume resume = resumeRepo.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        if (!resume.getJobseeker().getId().equals(seeker.getId())) {
            throw new BusinessException("You are not authorized to delete this resume");
        }

        try {
            String objectName = extractObjectName(resume.getFileURL());
            if (objectName != null && !objectName.isBlank()) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build());
            }
        } catch (Exception ex) {
            throw new BusinessException("Failed to delete resume file: " + ex.getMessage());
        }

        resumeRepo.delete(resume);
        return "Resume Deleted Successfully";
    }

    private void ensureBucket() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    private Resume.FileType parseFileType(String extension) {
        if ("pdf".equalsIgnoreCase(extension)) return Resume.FileType.PDF;
        if ("doc".equalsIgnoreCase(extension)) return Resume.FileType.DOC;
        if ("docx".equalsIgnoreCase(extension)) return Resume.FileType.DOCX;
        throw new BusinessException("Unsupported file type. Allowed: pdf, doc, docx");
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BusinessException("Invalid resume file");
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private String extractObjectName(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return null;
        }

        String marker = "/" + bucketName + "/";
        int markerIndex = fileUrl.indexOf(marker);
        if (markerIndex >= 0) {
            return fileUrl.substring(markerIndex + marker.length());
        }

        return fileUrl;
    }
}
