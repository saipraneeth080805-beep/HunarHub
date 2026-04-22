package com.hunarhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "entrepreneur_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrepreneurProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // General entrepreneur info
    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Business category: e.g. Cobbler, Tailor, Potter, etc.
    @Column(name = "business_category")
    private String businessCategory;

    // Shop-specific info (used especially for cobblers)
    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "shop_address", columnDefinition = "TEXT")
    private String shopAddress;

    @Column(name = "shop_phone")
    private String shopPhone;

    @Column(name = "shop_email")
    private String shopEmail;

    @Column(name = "shop_experience")
    private String shopExperience;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(name = "earnings", columnDefinition = "DECIMAL(10,2)")
    private Double earnings = 0.0;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}
