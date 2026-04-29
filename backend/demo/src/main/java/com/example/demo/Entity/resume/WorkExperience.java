package com.example.demo.Entity.resume;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import  com.example.demo.Entity.Jobseeker;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private Jobseeker jobSeeker;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private String location;

    @Column(length = 2000)
    private String description;
}
