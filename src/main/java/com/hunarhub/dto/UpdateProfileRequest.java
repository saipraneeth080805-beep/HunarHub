package com.hunarhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // For entrepreneurs
    private String skills;
    private String experience;
    private String description;
    private String businessCategory;
    private String shopName;
    private String ownerName;
    private String shopAddress;
    private String shopPhone;
    private String shopEmail;
    private String shopExperience;
    private String shopDescription;
}
