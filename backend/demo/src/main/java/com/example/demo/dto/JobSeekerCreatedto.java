package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerCreatedto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String headline;
    private String summary;

}
