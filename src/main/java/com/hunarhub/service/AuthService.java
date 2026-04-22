package com.hunarhub.service;

import com.hunarhub.dto.AuthResponse;
import com.hunarhub.dto.LoginRequest;
import com.hunarhub.dto.RegisterRequest;
import com.hunarhub.entity.EntrepreneurProfile;
import com.hunarhub.entity.User;
import com.hunarhub.exception.BadRequestException;
import com.hunarhub.repository.EntrepreneurProfileRepository;
import com.hunarhub.repository.UserRepository;
import com.hunarhub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntrepreneurProfileRepository entrepreneurProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.CUSTOMER);

        user = userRepository.save(user);

        // Create entrepreneur profile if role is ENTREPRENEUR
        if (user.getRole() == User.Role.ENTREPRENEUR) {
            EntrepreneurProfile profile = new EntrepreneurProfile();
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
            profile.setApprovalStatus(EntrepreneurProfile.ApprovalStatus.PENDING);

            // Basic validation for cobblers – require shop info
            if ("Cobbler".equalsIgnoreCase(request.getBusinessCategory())) {
                if (profile.getShopName() == null || profile.getShopName().isBlank()
                        || profile.getOwnerName() == null || profile.getOwnerName().isBlank()
                        || profile.getShopAddress() == null || profile.getShopAddress().isBlank()
                        || profile.getShopPhone() == null || profile.getShopPhone().isBlank()
                        || profile.getShopExperience() == null || profile.getShopExperience().isBlank()
                        || profile.getShopDescription() == null || profile.getShopDescription().isBlank()) {
                    throw new BadRequestException("Please fill all cobbler shop details to register as an entrepreneur.");
                }
            }

            entrepreneurProfileRepository.save(profile);

            List<User> admins = userRepository.findByRole(User.Role.ADMIN);
            for (User admin : admins) {
                notificationService.createNotification(
                        admin,
                        "Entrepreneur approval needed",
                        user.getName() + " registered as an entrepreneur and is awaiting your approval.",
                        "ENTREPRENEUR_PENDING",
                        null,
                        null,
                        null
                );
            }
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        String usernameOrEmail = request.getEmail().trim();
        User user = null;
        
        // Try to find user by email first
        user = userRepository.findByEmail(usernameOrEmail).orElse(null);
        
        // If not found by email, try to find by name (username)
        if (user == null) {
            user = userRepository.findByName(usernameOrEmail).orElse(null);
        }
        
        if (user == null) {
            throw new BadRequestException("Invalid credentials");
        }
        
        try {
            // Authenticate using the user's email (Spring Security uses email as username)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateToken(user.getEmail());

            return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail(), user.getRole());
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new BadRequestException("Invalid credentials");
        }
    }
}
