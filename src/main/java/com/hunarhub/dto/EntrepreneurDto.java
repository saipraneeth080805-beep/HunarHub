package com.hunarhub.dto;

import com.hunarhub.entity.EntrepreneurProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurDto {
    private Long id;
    private Long userId;
    private String name;
    private String email;
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
    private EntrepreneurProfile.ApprovalStatus approvalStatus;
    private Double earnings;
}
