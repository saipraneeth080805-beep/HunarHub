package com.hunarhub.service;

import com.hunarhub.dto.ChangePasswordRequest;
import com.hunarhub.dto.ProfileResponse;
import com.hunarhub.dto.UpdateProfileRequest;
import com.hunarhub.entity.EntrepreneurProfile;
import com.hunarhub.entity.User;
import com.hunarhub.exception.BadRequestException;
import com.hunarhub.exception.ResourceNotFoundException;
import com.hunarhub.repository.EntrepreneurProfileRepository;
import com.hunarhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntrepreneurProfileRepository entrepreneurProfileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                throw new RuntimeException("User not authenticated");
            }
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException("Error getting current user: " + e.getMessage(), e);
        }
    }

    public ProfileResponse getProfile() {
        try {
            User user = getCurrentUser();
            ProfileResponse response = new ProfileResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole() != null ? user.getRole().name() : null);
            
            // Safely get profile photo (may be null if column doesn't exist yet)
            try {
                response.setProfilePhoto(user.getProfilePhoto());
            } catch (Exception e) {
                response.setProfilePhoto(null);
            }
            
            response.setCreatedAt(user.getCreatedAt() != null 
                ? user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) 
                : null);

            // Add entrepreneur-specific fields if applicable
            if (user.getRole() == User.Role.ENTREPRENEUR) {
                try {
                    EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(user)
                            .orElse(null);
                    if (profile != null) {
                        response.setSkills(profile.getSkills());
                        response.setExperience(profile.getExperience());
                        response.setDescription(profile.getDescription());
                        response.setApprovalStatus(profile.getApprovalStatus() != null 
                            ? profile.getApprovalStatus().name() 
                            : null);
                        response.setBusinessCategory(profile.getBusinessCategory());
                        response.setShopName(profile.getShopName());
                        response.setOwnerName(profile.getOwnerName());
                        response.setShopAddress(profile.getShopAddress());
                        response.setShopPhone(profile.getShopPhone());
                        response.setShopEmail(profile.getShopEmail());
                        response.setShopExperience(profile.getShopExperience());
                        response.setShopDescription(profile.getShopDescription());
                    }
                } catch (Exception e) {
                    // If entrepreneur profile doesn't exist or has issues, just skip it
                    System.err.println("Error fetching entrepreneur profile: " + e.getMessage());
                }
            }

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching profile: " + e.getMessage(), e);
        }
    }

    @Transactional
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user = userRepository.save(user);

        // Update entrepreneur profile if applicable
        if (user.getRole() == User.Role.ENTREPRENEUR) {
            EntrepreneurProfile profile = entrepreneurProfileRepository.findByUser(user)
                    .orElse(new EntrepreneurProfile());
            profile.setUser(user);
            profile.setSkills(request.getSkills());
            profile.setExperience(request.getExperience());
            profile.setDescription(request.getDescription());
            profile.setBusinessCategory(request.getBusinessCategory());
            profile.setShopName(request.getShopName());
            profile.setOwnerName(request.getOwnerName());
            profile.setShopAddress(request.getShopAddress());
            profile.setShopPhone(request.getShopPhone());
            profile.setShopEmail(request.getShopEmail());
            profile.setShopExperience(request.getShopExperience());
            profile.setShopDescription(request.getShopDescription());
            if (profile.getId() == null) {
                profile.setApprovalStatus(EntrepreneurProfile.ApprovalStatus.PENDING);
            }
            entrepreneurProfileRepository.save(profile);
        }

        return getProfile();
    }

    @Transactional
    public ProfileResponse updateProfilePhoto(String photoUrl) {
        User user = getCurrentUser();
        
        // Delete old photo if exists
        if (user.getProfilePhoto() != null) {
            try {
                fileStorageService.deleteFile(user.getProfilePhoto());
            } catch (Exception e) {
                // Log error but continue
                System.err.println("Error deleting old profile photo: " + e.getMessage());
            }
        }

        user.setProfilePhoto(photoUrl);
        user = userRepository.save(user);

        return getProfile();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount() {
        User user = getCurrentUser();

        // Delete profile photo if exists
        if (user.getProfilePhoto() != null) {
            try {
                fileStorageService.deleteFile(user.getProfilePhoto());
            } catch (Exception e) {
                System.err.println("Error deleting profile photo: " + e.getMessage());
            }
        }

        // Delete entrepreneur profile if exists
        if (user.getRole() == User.Role.ENTREPRENEUR) {
            entrepreneurProfileRepository.findByUser(user).ifPresent(entrepreneurProfileRepository::delete);
        }

        // Delete user (cascade should handle related entities)
        userRepository.delete(user);
    }
}
