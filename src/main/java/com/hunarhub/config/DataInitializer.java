package com.hunarhub.config;

import com.hunarhub.entity.Category;
import com.hunarhub.entity.User;
import com.hunarhub.repository.CategoryRepository;
import com.hunarhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default categories if they don't exist
        String[] categoryNames = {"Cobbler", "Potter", "Tailor", "Artisan"};
        
        for (String name : categoryNames) {
            if (!categoryRepository.findByName(name).isPresent()) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            }
        }
        
        // Initialize admin user with fixed credentials
        // Admin can login with email: adminhunarhub@hunarhub.com OR username: adminhunarhub
        String adminEmail = "adminhunarhub@hunarhub.com";
        String adminUsername = "adminhunarhub";
        
        // Check if admin exists with old email format and update it
        userRepository.findByEmail("adminhunarhub").ifPresent(oldAdmin -> {
            if (oldAdmin.getRole() == User.Role.ADMIN) {
                oldAdmin.setEmail(adminEmail);
                oldAdmin.setName(adminUsername); // Ensure name is set correctly
                userRepository.save(oldAdmin);
                System.out.println("Admin email updated to valid format!");
            }
        });
        
        // Check if admin exists by email or name
        User existingAdmin = userRepository.findByEmail(adminEmail).orElse(null);
        if (existingAdmin == null) {
            existingAdmin = userRepository.findByName(adminUsername).orElse(null);
        }
        
        // Create or update admin if needed
        if (existingAdmin == null) {
            // Create new admin
            User admin = new User();
            admin.setName(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("hunarhubadminaccess"));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
            System.out.println("Admin can login with email: " + adminEmail + " OR username: " + adminUsername);
        } else if (existingAdmin.getRole() == User.Role.ADMIN) {
            // Update existing admin to ensure correct credentials
            boolean updated = false;
            if (!adminEmail.equals(existingAdmin.getEmail())) {
                existingAdmin.setEmail(adminEmail);
                updated = true;
            }
            if (!adminUsername.equals(existingAdmin.getName())) {
                existingAdmin.setName(adminUsername);
                updated = true;
            }
            // Always update password to ensure it's correct
            existingAdmin.setPassword(passwordEncoder.encode("hunarhubadminaccess"));
            userRepository.save(existingAdmin);
            if (updated) {
                System.out.println("Admin user updated successfully!");
            }
            System.out.println("Admin can login with email: " + adminEmail + " OR username: " + adminUsername);
        }
    }
}
