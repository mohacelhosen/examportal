package com.mcu.examportal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String gender;
    private String password;
    private String designation;
    private String userPhoto;
    private String linkedin;
    private String github;
    private String facebook;
}
