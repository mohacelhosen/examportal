package com.mcu.examportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userProfileId;
    private String userPhoto;
    private String linkedin;
    private String github;
    private String facebook;

}
