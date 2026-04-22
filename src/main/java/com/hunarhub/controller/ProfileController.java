package com.hunarhub.controller;

import com.hunarhub.dto.ChangePasswordRequest;
import com.hunarhub.dto.ProfileResponse;
import com.hunarhub.dto.UpdateProfileRequest;
import com.hunarhub.service.FileStorageService;
import com.hunarhub.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.ok(profileService.getProfile());
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("column") && errorMessage.contains("profile_photo")) {
                error.put("error", "Database schema needs to be updated. Please restart the Spring Boot application.");
                error.put("details", "The profile_photo column doesn't exist yet. Restarting the app will create it automatically.");
            } else {
                error.put("error", "Failed to fetch profile: " + (errorMessage != null ? errorMessage : e.getClass().getSimpleName()));
            }
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }

    @PostMapping("/photo")
    public ResponseEntity<Map<String, String>> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileStorageService.storeFile(file);
            ProfileResponse profile = profileService.updateProfilePhoto(fileUrl);
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            response.put("message", "Profile photo updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to upload profile photo: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAccount() {
        profileService.deleteAccount();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        return ResponseEntity.ok(response);
    }
}
