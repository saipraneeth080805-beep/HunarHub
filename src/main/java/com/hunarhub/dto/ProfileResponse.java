package com.hunarhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String profilePhoto;
    private String createdAt;
    
    // For entrepreneurs
    private String skills;
    private String experience;
    private String description;
    private String approvalStatus;
    private String businessCategory;
    private String shopName;
    private String ownerName;
    private String shopAddress;
    private String shopPhone;
    private String shopEmail;
    private String shopExperience;
    private String shopDescription;
}
