package com.hunarhub.dto;

import com.hunarhub.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private User.Role role;

    // For entrepreneur registration
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
