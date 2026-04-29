package com.example.demo.Entity.resume;

import com.example.demo.Entity.Jobseeker;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private Jobseeker jobseeker;

    @Column(nullable = false)
    private String fileURL;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    @Column(nullable = false)
    private boolean isDefault;

    @Column(nullable = false)
    private String visibility;

    public enum FileType {
        PDF, DOC, DOCX
    }
}